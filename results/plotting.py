import argparse
from pathlib import Path

import numpy as np
import pandas as pd
import matplotlib.pyplot as plt


def load_comsol_table(path):
    """
    Load the numeric part of a COMSOL text-table export.

    Expected current column order:
      lda0, T, air_domain_length, w0, freq,
      BS1_attenuation_factor, BS3_attenuation_factor,
      C_o_fraction, T_o_fraction

    If a 10th numeric column is present, it is treated as Pkept.
    Air length is in mm; beam waist is in m, as in the supplied headers.
    """
    df = pd.read_csv(path, comment="%", sep=r"\s+", header=None)

    base_cols = [
        "lda0",
        "T",
        "air_domain_length",
        "w0",
        "freq",
        "BS1_attenuation_factor",
        "BS3_attenuation_factor",
        "C_o_fraction",
        "T_o_fraction",
    ]

    if df.shape[1] == 9:
        df.columns = base_cols
    elif df.shape[1] == 10:
        df.columns = base_cols + ["Pkept"]
    else:
        raise ValueError(
            f"Expected 9 or 10 numeric columns, but found {df.shape[1]}. "
            "Update load_comsol_table() to match the export."
        )

    return df


def choose_heatmap_axes(df):
    """
    Heatmaps always use wavelength and temperature.
    Whichever variable has MORE unique values goes on the horizontal axis.
    If tied, wavelength is horizontal.
    """
    wavelengths = np.sort(df["lda0"].unique())
    temperatures = np.sort(df["T"].unique())

    if len(wavelengths) >= len(temperatures):
        return "lda0", "T", "Wavelength (nm)", "Temperature (K)"
    else:
        return "T", "lda0", "Temperature (K)", "Wavelength (nm)"


def pretty_w0(value):
    return f"{value * 1e3:g} mm"


def plot_metric_grid(df, metric, title, output_dir, annotate=True):
    """
    One figure per metric.

    Panel rows    = air_domain_length
    Panel columns = w0

    Inside every panel:
      axes = wavelength and temperature,
      with the variable having more unique values placed horizontally.
    """
    air_lengths = np.sort(df["air_domain_length"].unique())
    w0_values = np.sort(df["w0"].unique())

    x_col, y_col, x_label, y_label = choose_heatmap_axes(df)

    # One common color scale across every panel for this metric.
    vmin = df[metric].min()
    vmax = df[metric].max()

    nrows = len(air_lengths)
    ncols = len(w0_values)

    fig, axes = plt.subplots(
        nrows,
        ncols,
        figsize=(4.1 * ncols, 3.5 * nrows),
        squeeze=False,
        constrained_layout=True,
    )

    last_im = None

    for r, air_len in enumerate(air_lengths):
        for c, w0 in enumerate(w0_values):
            ax = axes[r, c]

            subset = df[
                np.isclose(df["air_domain_length"], air_len)
                & np.isclose(df["w0"], w0)
            ].copy()

            heat = subset.pivot_table(
                index=y_col,
                columns=x_col,
                values=metric,
                aggfunc="mean",
            )

            # Ensure numeric axes are sorted.
            heat = heat.reindex(index=np.sort(df[y_col].unique()),
                                columns=np.sort(df[x_col].unique()))

            x_vals = heat.columns.to_numpy()
            y_vals = heat.index.to_numpy()
            z = heat.to_numpy()

            last_im = ax.imshow(
                z,
                aspect="auto",
                origin="lower",
                vmin=vmin,
                vmax=vmax,
            )

            ax.set_xticks(np.arange(len(x_vals)))
            ax.set_xticklabels([f"{v:g}" for v in x_vals])
            ax.set_yticks(np.arange(len(y_vals)))
            ax.set_yticklabels([f"{v:g}" for v in y_vals])

            # Keep labels only on outside plots to reduce clutter.
            if r == nrows - 1:
                ax.set_xlabel(x_label)
            if c == 0:
                ax.set_ylabel(y_label)

            ax.set_title(
                f"Air length = {air_len:g} mm\n"
                f"$w_0$ = {pretty_w0(w0)}"
            )

            if annotate:
                span = vmax - vmin
                midpoint = vmin + span / 2 if span > 0 else vmin

                for iy in range(z.shape[0]):
                    for ix in range(z.shape[1]):
                        value = z[iy, ix]
                        if np.isnan(value):
                            continue

                        # Let matplotlib choose ordinary plot colors; only
                        # annotation contrast is switched for readability.
                        text_color = "white" if value < midpoint else "black"
                        ax.text(
                            ix,
                            iy,
                            f"{value:.4f}",
                            ha="center",
                            va="center",
                            fontsize=8,
                            color=text_color,
                        )

    fig.suptitle(title, fontsize=16)

    if last_im is not None:
        cbar = fig.colorbar(last_im, ax=axes, shrink=0.88)
        cbar.set_label(metric)

    output_dir.mkdir(parents=True, exist_ok=True)
    safe_name = metric.replace("/", "_").replace(" ", "_")
    png_path = output_dir / f"{safe_name}_heatmaps.png"
    pdf_path = output_dir / f"{safe_name}_heatmaps.pdf"

    fig.savefig(png_path, dpi=250, bbox_inches="tight")
    fig.savefig(pdf_path, bbox_inches="tight")
    plt.close(fig)

    print(f"Saved: {png_path}")
    print(f"Saved: {pdf_path}")


def main():
    parser = argparse.ArgumentParser(
        description="Plot LOQC COMSOL parameter-sweep heatmaps."
    )
    parser.add_argument("files", nargs=2, help="Two COMSOL text exports to combine")
    parser.add_argument(
        "--output-dir",
        default="loqc_plots",
        help="Directory for output plots",
    )
    parser.add_argument(
        "--no-annotations",
        action="store_true",
        help="Do not print numeric values inside heatmap cells",
    )
    args = parser.parse_args()

    # These exports cover different air lengths: concatenate, do not average.
    df = pd.concat([load_comsol_table(path) for path in args.files], ignore_index=True)
    # Header units are mm: keep 50, 75 and 100 mm (exclude 25 mm).
    keep = np.isclose(df["air_domain_length"].to_numpy()[:, None],
                      np.array([50.0, 75.0, 100.0])[None, :]).any(axis=1)
    removed = int((~keep).sum())
    df = df.loc[keep].copy()
    if df.empty:
        raise ValueError("No rows remain for air lengths 50, 75, 100 mm.")
    keys = ["lda0", "T", "air_domain_length", "w0"]
    if df.duplicated(keys).any():
        raise ValueError("The files contain overlapping parameter combinations; "
                         "resolve duplicates before plotting (no averaging applied).")
    df = df.sort_values(keys).reset_index(drop=True)
    print(f"Combined inputs; removed {removed} rows; retained {len(df)} rows.")
    outdir = Path(args.output_dir)

    print("Unique sweep values:")
    print("  wavelength:", sorted(df["lda0"].unique()))
    print("  temperature:", sorted(df["T"].unique()))
    print("  air length:", sorted(df["air_domain_length"].unique()))
    print("  w0:", sorted(df["w0"].unique()))

    x_col, y_col, x_label, y_label = choose_heatmap_axes(df)
    print(f"Heatmap horizontal axis: {x_label}")
    print(f"Heatmap vertical axis:   {y_label}")

    metrics = [
        (
            "BS1_attenuation_factor",
            "BS1 attenuation",
        ),
        (
            "BS3_attenuation_factor",
            "BS3 attenuation",
        ),
    ]

    # Pkept can be reconstructed from the exported normalized quantities:
    #
    #   BS1_attenuation_factor = BS1_bin / Pin
    #   BS3_attenuation_factor = BS3_bin / Pin
    #   C_o_fraction           = C_o / (2*Pin)
    #   T_o_fraction           = T_o / (2*Pin)
    #
    # Therefore:
    #
    #   Pkept = (BS1_bin + BS3_bin + C_o + T_o) / (2*Pin)
    #         = 0.5*BS1_attenuation_factor
    #         + 0.5*BS3_attenuation_factor
    #         + C_o_fraction
    #         + T_o_fraction
    #
    # If COMSOL already exported Pkept explicitly, keep that value.
    if "Pkept" not in df.columns:
        df["Pkept"] = (
            0.5 * df["BS1_attenuation_factor"]
            + 0.5 * df["BS3_attenuation_factor"]
            + df["C_o_fraction"]
            + df["T_o_fraction"]
        )

    metrics.append(("Pkept", "Pkept"))

    for metric, title in metrics:
        plot_metric_grid(
            df,
            metric=metric,
            title=title,
            output_dir=outdir,
            annotate=not args.no_annotations,
        )


if __name__ == "__main__":
    main()
