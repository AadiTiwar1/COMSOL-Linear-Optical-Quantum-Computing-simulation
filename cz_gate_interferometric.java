/*
 * cz_gate_interferometric.java
 */

import com.comsol.model.*;
import com.comsol.model.util.*;

/** Model exported on Oct 17 2026, 15:37 by COMSOL 6.4.0.293. */
public class cz_gate_interferometric {

  public static Model run() {
    Model model = ModelUtil.create("Model");

    model.modelPath("C:\\Users\\atiwari306\\Downloads");

    model.label("cz_gate_interferometric - error inspection.mph");

    model.title("Polarizing Beam Splitter");

    model
         .description("A Gaussian beam is incident on a 45-degree thin-film stack embedded in glass material prisms. The thin-film stack is designed from alternating high and low refractive index materials. The wave will be refracted at the Brewster angle at each internal interface. Thus, mainly p-polarized waves (polarization in the plane of incidence) will be transmitted, whereas mainly s-polarized waves (polarization orthogonal to the plane of incidence) will be reflected. Changing the spot radius for the Gaussian beam modifies the polarization discrimination.\n\nThe reflectance and transmittance spectra are calculated for different Gaussian beam spot radii.\n\nThe app automatically calculates the phase expressions necessary for the Electromagnetic Waves, Beam Envelopes interface, when the user changes the design parameters.");

    model.colorTheme("COMSOL");

    model.param().set("wl0", "900[nm]", "Center wavelength");
    model.param().set("dWl", "200[nm]", "Bandwidth");
    model.param()
         .set("w0", "9.000000000000001E-4[m]", "Spot radius, effects rayleigh range and side length 'a' of PBS. last val: 4");
    model.param().set("nPrism_DESIGN", "1.6586", "Refractive index of the glass prisms");
    model.param().set("nHigh_DESIGN", "2.3132", "Refractive index of the high-index materials");
    model.param().set("nLow_DESIGN", "1.3751", "Refractive index of the low-index material");
    model.param().set("z0", "pi*nPrism*w0^2/wl0", "Rayleigh range");
    model.param()
         .set("a", "8*w0/sqrt(1-(8*w0/z0)^2)", "Prism side. Originally multiplied by 8, i changed to 1662 to resemble the 30mm thorlabs PBS. Realized 1662 would kill RAM and caused tolerance meshing issues, so nerfed back to 8. Unsure of significance of 8 or 1662.");
    model.param().set("nLayers", "7", "Number of dielectric layers");
    model.param().set("dHigh", "wl0/4/nHigh/cos(alphaHigh)", "Thickness of high-index material layers");
    model.param().set("dLow", "wl0/4/nLow/cos(alphaLow)", "Thickness of low-index material layers");
    model.param().set("E0", "1[V/m]", "Peak incident electric field");
    model.param().set("lda0", "800[nm]", "Wavelength in vacuum");
    model.param().set("k0", "2*pi/lda0", "Wave number in vacuum");
    model.param().set("kParallel", "k0*nPrism/sqrt(2)", "Wave vector component parallel to the boundaries");
    model.param()
         .set("kHighNormal", "k0*sqrt(nHigh^2-nPrism^2/2)", "Wave vector component in high-index materials in the normal direction");
    model.param()
         .set("kLowNormal", "k0*sqrt(nLow^2-nPrism^2/2)", "Wave vector component in the normal direction for low-index materials");
    model.param().set("dStack", "floor(nLayers/2)*(dHigh+dLow)+dHigh", "Thickness of thin-film stack");
    model.param().set("wlCount", "3", "Number of wavelengths in the sweep");
    model.param().set("alphaHigh", "asin(nPrism*sin(pi/4)/nHigh)", "Angle in high-index material");
    model.param().set("alphaLow", "asin(nPrism*sin(pi/4)/nLow)", "Angle in low-index material");
    model.param().set("xCOGu", "-(a/2+dStack/sqrt(2))/3", "Center of gravity, upper triangle, x-coordinate");
    model.param().set("yCOGu", "-xCOGu", "Center of gravity, upper triangle, y-coordinate");
    model.param().set("xCOGl", "-xCOGu", "Center of gravity, lower triangle, x-coordinate");
    model.param().set("yCOGl", "xCOGu", "Center of gravity, lower triangle, y-coordinate");
    model.param().set("currentiter", "0");
    model.param().group().create("par2");
    model.param("par2").set("n_air", "1", "Aadi's Simple air");
    model.param("par2")
         .set("n_antireflection", "sqrt(nPrism*n_air)", "Geometric mean of the two materials, I guess this is supposed to work? Corroborats with both the optical discord guy and GPT");
    model.param("par2")
         .set("d_antireflection", "(800[nm])/(4*n_antireflection)", "Corresponding thickness of AR coating");
    model.param("par2")
         .set("air_domain_length", "25[mm]", "Used to parameterize the length of air domains to see how beam propagation affects loss. Was originally 250um when w0=20*wl0.");
    model.param("par2").set("pml_thickness", "100*2[um]");
    model.param("par2")
         .set("silver_film_thickness", "7[nm]", "Thickness of the normal beamsplitters silver film, found via parameter sweep in normal BS model");
    model.param("par2").set("eps_Ag", "-16.5-1.06*i", "Relative dielectric constant, Silver");
    model.param("par2").set("n_Ag_DESIGN", "sqrt(eps_Ag)", "n^2 = permitivvity");
    model.param("par2")
         .set("Temp", "300[K]", "Will parameter sweep later for environment's affect on refractive indices");
    model.param().group().create("par3");
    model.param("par3").set("T", "293.0");
    model.param("par3").set("x_lda", "lda0*(10^6[1/m])");
    model.param("par3")
         .set("s1_ZnS", "3.35933-0.000512262*T+0.0000101086*T^(2)-0.0000000414798*T^(3)+0.0000000000691051*T^(4)", "Temperature-dependent refractive index of Cleartran\u00ae ZnS to cryogenic temperatures Doug Leviton and Brad Frey I NASA Goddard Space Flight Center");
    model.param("par3")
         .set("s2_ZnS", "0.706131+0.000489603*T-0.00000891159*T^(2)+0.0000000381621*T^(3)-0.0000000000654805*T^(4)");
    model.param("par3")
         .set("s3_ZnS", "4.02154-0.0293193*T+0.000231080*T^(2)-0.000000757289*T^(3)+0.000000000831188*T^(4)");
    model.param("par3")
         .set("l1_ZnS", "0.161151-0.00000893057*T+0.000000273286*T^(2)-0.00000000123408*T^(3)+0.00000000000229917*T^(4)");
    model.param("par3")
         .set("l2_ZnS", "0.282427-0.0000466636*T+0.000000755906*T^(2)-0.00000000277513*T^(3)+0.00000000000435237*T^(4)");
    model.param("par3")
         .set("l3_ZnS", "41.1590-0.161010*T+0.00123906*T^(2)-0.00000395895*T^(3)+0.00000000416370*T^(4)");
    model.param("par3")
         .set("n_ZnS", "sqrt(1+s1_ZnS*x_lda^2/(x_lda^2-l1_ZnS^2)+s2_ZnS*x_lda^2/(x_lda^2-l2_ZnS^2)+s3_ZnS*x_lda^2/(x_lda^2-l3_ZnS^2))-0.001", "2.3132 from og app builder, 800nm room temp (293K?)");
    model.param("par3").set("nHigh", "n_ZnS");
    model.param().group().create("par5");
    model.param("par5")
         .set("S1_MgF2", "0.49948850-2.62696e-5*(T)-4.99110e-8*(T)^2", "https://www.spiedigitallibrary.org/journals/optical-engineering/volume-34/issue-05/0000/Temperature-dependent-refractive-index-models-for-BaF2-CaF2-MgF2-SrF2/10.1117/12.201666.full");
    model.param("par5").set("S2_MgF2", "0.38552565+3.30746e-5*(T)+4.16987e-8*(T)^2");
    model.param("par5").set("S3_MgF2", "2.3187214+5.38686e-6*(T)-9.67447e-8*(T)^2");
    model.param("par5")
         .set("n_MgF2", "sqrt(1+S1_MgF2*x_lda^2/(x_lda^2-0.04338408^2)+S2_MgF2*x_lda^2/(x_lda^2-0.09461442^2)+S3_MgF2*x_lda^2/(x_lda^2-23.793604^2))", "1.3751 from og app builder, 800nm room temp (293K?)");
    model.param("par5").set("nLow", "n_MgF2");
    model.param().group().create("par4");
    model.param("par4")
         .set("nSF5_ref", "sqrt(1+1.46141885*x_lda^2/(x_lda^2-0.0111826126)+0.247713019*x_lda^2/(x_lda^2-0.0508594669)+0.949995832*x_lda^2/(x_lda^2-112.041888))", "Non temperature dependent Sellmeier equation from schott datasheet");
    model.param("par4")
         .set("dT", "T-293.15", "Difference between current temperature and measured reference temperature, room temp weather");
    model.param("par4")
         .set("n_SF5", "nSF5_ref+((nSF5_ref^2-1)/(2*nSF5_ref))*(2.59e-6*dT+1.76e-8*dT^2-2.03e-11*dT^3+(1.17e-6*dT+1.09e-9*dT^2)/(x_lda^2-0.255^2))", "1.6586 at 800nm, room temp (293K?)");
    model.param("par4").set("nPrism", "n_SF5");
    model.param().group().create("par6");
    model.param("par6").set("w_omega", "(2*pi*(2998*10^8[m/s]))/lda0", "Angular frequency");
    model.param("par6")
         .set("E_photon", "(6.242*10^18)*(6.62607*10^-34)*(2.998*10^8[m])/(lda0)", "Energy in each photon converted to eV");
    model.param("par6")
         .set("T_C", "(T-273.15[K])/1[K]", "their drude lorentz equation uses celsius instead of kelvin");
    model.param("par6").set("eps_inf_34", "3.38 + 0.00254237*(T_C-23)", "Extrapolating between 23 and 100K");
    model.param("par6").set("wp_34", "8.87 + 0.00192090*(T_C-23)", "Extrapolating between 23 and 100K");
    model.param("par6").set("GammaD_34", "0.026 + 0.000135593*(T_C-23)", "Extrapolating between 23 and 100K");
    model.param("par6").set("A1_34", "7.27 - 0.00293785*(T_C-23)", "Extrapolating between 23 and 100K");
    model.param("par6").set("gamma1_34", "0.14 + 0.000338983*(T_C-23)", "Extrapolating between 23 and 100K");
    model.param("par6").set("E1_34", "4.15 - 0.000338983*(T_C-23)", "Extrapolating between 23 and 100K");
    model.param("par6")
         .set("eps_Ag_34", "eps_inf_34-wp_34^2/(E_photon^2+i*GammaD_34*E_photon)+A1_34/(E1_34^2-E_photon^2-i*gamma1_34*E_photon)", "Extrapolating between 23 and 100K");
    model.param("par6").set("n_Ag", "sqrt(eps_Ag_34)");
    model.param().label("Parameters 1 (from PBS app builder)");
    model.param("par2").label("Parameters 2 (from Aadi)");
    model.param("par3").label("ZnS Thermo-Optic Sellmeier");
    model.param("par5").label("MgF2 Thermo-Optic Sellmeier");
    model.param("par4").label("SF5");
    model.param("par6").label("Silver Thin Film 34nm");

    model.component().create("comp1", true);

    model.component("comp1").geom().create("geom1", 2);

    model.result().table().create("tbl1", "Table");
    model.result().table().create("tbl2", "Table");
    model.result().table().create("tbl5", "Table");
    model.result().table().create("tbl7", "Table");
    model.result().table().create("tbl8", "Table");
    model.result().table().create("tbl9", "Table");
    model.result().table().create("evl2", "Table");

    model.component("comp1").mesh().create("mesh1");

    model.geom().create("part1", "Part", 2);
    model.geom("part1").label("Thin-Film Stack");
    model.geom("part1").inputParam().set("nLayers", "3", "Number of layers");
    model.geom("part1").inputParam().set("dHigh", "1[um]", "Thickness of high-index material");
    model.geom("part1").inputParam().set("dLow", "2[um]", "Thickness of low-index material");
    model.geom("part1").inputParam().set("height", "5[um]", "Height of layers");
    model.geom("part1").selection().create("csel1", "CumulativeSelection");
    model.geom("part1").selection("csel1").label("High-Index Material");
    model.geom("part1").selection().create("csel2", "CumulativeSelection");
    model.geom("part1").selection("csel2").label("Low-Index Material");
    model.geom("part1").selection().create("csel3", "CumulativeSelection");
    model.geom("part1").selection("csel3").label("Thin-Film Stack");
    model.geom("part1").create("pch1", "ParameterCheck");
    model.geom("part1").feature("pch1").set("condition", "nLayers<1");
    model.geom("part1").feature("pch1").set("message", "The number of layers must be a positive odd number");
    model.geom("part1").create("pch2", "ParameterCheck");
    model.geom("part1").feature("pch2").set("condition", "mod(nLayers,2)==0");
    model.geom("part1").feature("pch2").set("message", "The number of layers must be a positive odd number");
    model.geom("part1").create("r1", "Rectangle");
    model.geom("part1").feature("r1").label("First High-Index Layer");
    model.geom("part1").feature("r1").set("size", new String[]{"dHigh", "height"});
    model.geom("part1").feature("r1")
         .set("pos", new String[]{"-floor(nLayers/2)/2*(dHigh+dLow)-dHigh/2", "-height/2"});
    model.geom("part1").feature("r1").set("contributeto", "csel1");
    model.geom("part1").create("if1", "If");
    model.geom("part1").feature("if1").set("condition", "nLayers > 1");
    model.geom("part1").create("copy1", "Copy");
    model.geom("part1").feature("copy1").label("Last High-Index Layer");
    model.geom("part1").feature("copy1").set("displx", "floor(nLayers/2)*(dHigh+dLow)");
    model.geom("part1").feature("copy1").set("contributeto", "csel1");
    model.geom("part1").feature("copy1").selection("input").set("r1");
    model.geom("part1").create("r2", "Rectangle");
    model.geom("part1").feature("r2").label("First Low-Index Layer");
    model.geom("part1").feature("r2").set("size", new String[]{"dLow", "height"});
    model.geom("part1").feature("r2")
         .set("pos", new String[]{"-floor(nLayers/2)/2*(dHigh+dLow)+dHigh/2", "-height/2"});
    model.geom("part1").feature("r2").set("contributeto", "csel2");
    model.geom("part1").create("arr1", "Array");
    model.geom("part1").feature("arr1").set("fullsize", new String[]{"floor(nLayers/2)", "1"});
    model.geom("part1").feature("arr1").set("displ", new String[]{"dHigh+dLow", "0"});
    model.geom("part1").feature("arr1").selection("input").set("r1", "r2");
    model.geom("part1").create("endif1", "EndIf");
    model.geom("part1").create("uni1", "Union");
    model.geom("part1").feature("uni1").set("contributeto", "csel3");
    model.geom("part1").feature("uni1").selection("input").set("arr1", "copy1");
    model.geom("part1").run();
    model.component("comp1").geom("geom1").lengthUnit("mm");
    model.component("comp1").geom("geom1").scaleUnitValue(true);
    model.component("comp1").geom("geom1").selection().create("csel1", "CumulativeSelection");
    model.component("comp1").geom("geom1").selection("csel1").label("Prisms");
    model.component("comp1").geom("geom1").create("r2", "Rectangle");
    model.component("comp1").geom("geom1").feature("r2").label("left_vertical_arm section 1");
    model.component("comp1").geom("geom1").feature("r2").set("size", new String[]{"a", "air_domain_length"});
    model.component("comp1").geom("geom1").feature("r2").set("pos", new String[]{"-0.5*a", "0.5*a"});
    model.component("comp1").geom("geom1").create("r3", "Rectangle");
    model.component("comp1").geom("geom1").feature("r3").label("bottom_horizontal_arm  section 1");
    model.component("comp1").geom("geom1").feature("r3").set("size", new String[]{"air_domain_length", "a"});
    model.component("comp1").geom("geom1").feature("r3").set("pos", new String[]{"0.5*a", "-0.5*a"});
    model.component("comp1").geom("geom1").create("r4", "Rectangle");
    model.component("comp1").geom("geom1").feature("r4").label("top_horizontal_arm section 1");
    model.component("comp1").geom("geom1").feature("r4").set("size", new String[]{"air_domain_length", "a"});
    model.component("comp1").geom("geom1").feature("r4")
         .set("pos", new String[]{"0.5*a", "0.5*a+air_domain_length"});
    model.component("comp1").geom("geom1").create("r5", "Rectangle");
    model.component("comp1").geom("geom1").feature("r5").label("right_vertical_arm section 1");
    model.component("comp1").geom("geom1").feature("r5").set("size", new String[]{"a", "air_domain_length"});
    model.component("comp1").geom("geom1").feature("r5")
         .set("pos", new String[]{"0.5*a+air_domain_length", "0.5*a"});
    model.component("comp1").geom("geom1").create("pi1", "PartInstance");
    model.component("comp1").geom("geom1").feature("pi1").setEntry("inputexpr", "nLayers", "nLayers");
    model.component("comp1").geom("geom1").feature("pi1").setEntry("inputexpr", "dHigh", "dHigh");
    model.component("comp1").geom("geom1").feature("pi1").setEntry("inputexpr", "dLow", "dLow");
    model.component("comp1").geom("geom1").feature("pi1").setEntry("inputexpr", "height", "sqrt(2)*a-dStack");
    model.component("comp1").geom("geom1").feature("pi1").set("rot", -45);
    model.component("comp1").geom("geom1").feature("pi1").set("selkeepnoncontr", false);
    model.component("comp1").geom("geom1").feature("pi1").setEntry("selkeepobj", "pi1_csel1", "on");
    model.component("comp1").geom("geom1").feature("pi1").setEntry("selkeepobj", "pi1_csel2", "on");
    model.component("comp1").geom("geom1").feature("pi1").setEntry("selkeepobj", "pi1_csel3", "on");
    model.component("comp1").geom("geom1").feature("pi1").setEntry("selkeepdom", "pi1_csel1.dom", "on");
    model.component("comp1").geom("geom1").feature("pi1").setEntry("selkeepdom", "pi1_csel2.dom", "on");
    model.component("comp1").geom("geom1").feature("pi1").setEntry("selkeepdom", "pi1_csel3.dom", "on");
    model.component("comp1").geom("geom1").feature("pi1").setEntry("selkeepbnd", "pi1_csel2.bnd", "on");
    model.component("comp1").geom("geom1").feature("pi1").setEntry("selkeepbnd", "pi1_csel3.bnd", "on");
    model.component("comp1").geom("geom1").create("pol1", "Polygon");
    model.component("comp1").geom("geom1").feature("pol1").label("Polygon 1, bottom left trapezoid");
    model.component("comp1").geom("geom1").feature("pol1").set("source", "table");
    model.component("comp1").geom("geom1").feature("pol1")
         .set("table", new String[][]{{"-0.5*a", "-0.5*a+dStack/sqrt(2)"}, {"-0.5*dStack/sqrt(2)", "0.5*dStack/sqrt(2)"}, {"xCOGu", "yCOGu"}, {"-0.5*a", "0.5*dStack/sqrt(2)"}});
    model.component("comp1").geom("geom1").create("pol2", "Polygon");
    model.component("comp1").geom("geom1").feature("pol2").label("Polygon 2, top left trapezoid");
    model.component("comp1").geom("geom1").feature("pol2").set("source", "table");
    model.component("comp1").geom("geom1").feature("pol2")
         .set("table", new String[][]{{"xCOGu", "yCOGu"}, {"-0.5*a", "0.5*dStack/sqrt(2)"}, {"-0.5*a", "0.5*a"}, {"-0.5*dStack/sqrt(2)", "0.5*a"}});
    model.component("comp1").geom("geom1").create("pol3", "Polygon");
    model.component("comp1").geom("geom1").feature("pol3").label("Polygon 3, top right trapezoid");
    model.component("comp1").geom("geom1").feature("pol3").set("source", "table");
    model.component("comp1").geom("geom1").feature("pol3")
         .set("table", new String[][]{{"-0.5*dStack/sqrt(2)", "0.5*a"}, {"xCOGu", "yCOGu"}, {"-0.5*dStack/sqrt(2)", "0.5*dStack/sqrt(2)"}, {"0.5*a-dStack/sqrt(2)", "0.5*a"}});
    model.component("comp1").geom("geom1").create("uni1", "Union");
    model.component("comp1").geom("geom1").feature("uni1").set("repairtoltype", "absolute");
    model.component("comp1").geom("geom1").feature("uni1").set("absrepairtol", 1.5E-5);
    model.component("comp1").geom("geom1").feature("uni1").selection("input").set("pol1", "pol2", "pol3");
    model.component("comp1").geom("geom1").create("rot1", "Rotate");
    model.component("comp1").geom("geom1").feature("rot1").label("Rotate top prism to make bottom prism");
    model.component("comp1").geom("geom1").feature("rot1").set("keep", true);
    model.component("comp1").geom("geom1").feature("rot1").setIndex("rot", "180", 0);
    model.component("comp1").geom("geom1").feature("rot1").set("contributeto", "csel1");
    model.component("comp1").geom("geom1").feature("rot1").selection("input").set("uni1");
    model.component("comp1").geom("geom1").create("rot2", "Rotate");
    model.component("comp1").geom("geom1").feature("rot2").label("copy right prism, rotated PBS2");
    model.component("comp1").geom("geom1").feature("rot2").set("keep", true);
    model.component("comp1").geom("geom1").feature("rot2").setIndex("rot", "180", 0);
    model.component("comp1").geom("geom1").feature("rot2").set("contributeto", "csel1");
    model.component("comp1").geom("geom1").feature("rot2").selection("input").set("uni1");
    model.component("comp1").geom("geom1").create("rot3", "Rotate");
    model.component("comp1").geom("geom1").feature("rot3").label("copy left prism PBS2");
    model.component("comp1").geom("geom1").feature("rot3").set("keep", true);
    model.component("comp1").geom("geom1").feature("rot3").set("contributeto", "csel1");
    model.component("comp1").geom("geom1").feature("rot3").selection("input").set("uni1");
    model.component("comp1").geom("geom1").create("pi2", "PartInstance");
    model.component("comp1").geom("geom1").feature("pi2").label("Thin-Film Stack 2 PBS2");
    model.component("comp1").geom("geom1").feature("pi2").setEntry("inputexpr", "nLayers", "nLayers");
    model.component("comp1").geom("geom1").feature("pi2").setEntry("inputexpr", "dHigh", "dHigh");
    model.component("comp1").geom("geom1").feature("pi2").setEntry("inputexpr", "dLow", "dLow");
    model.component("comp1").geom("geom1").feature("pi2").setEntry("inputexpr", "height", "sqrt(2)*a-dStack");
    model.component("comp1").geom("geom1").feature("pi2").set("rot", -45);
    model.component("comp1").geom("geom1").feature("pi2").set("selkeepnoncontr", false);
    model.component("comp1").geom("geom1").feature("pi2").setEntry("selkeepobj", "pi2_csel1", "on");
    model.component("comp1").geom("geom1").feature("pi2").setEntry("selkeepobj", "pi2_csel2", "on");
    model.component("comp1").geom("geom1").feature("pi2").setEntry("selkeepobj", "pi2_csel3", "on");
    model.component("comp1").geom("geom1").feature("pi2").setEntry("selkeepdom", "pi2_csel1.dom", "on");
    model.component("comp1").geom("geom1").feature("pi2").setEntry("selkeepdom", "pi2_csel2.dom", "on");
    model.component("comp1").geom("geom1").feature("pi2").setEntry("selkeepdom", "pi2_csel3.dom", "on");
    model.component("comp1").geom("geom1").feature("pi2").setEntry("selkeepbnd", "pi2_csel2.bnd", "on");
    model.component("comp1").geom("geom1").feature("pi2").setEntry("selkeepbnd", "pi2_csel3.bnd", "on");
    model.component("comp1").geom("geom1").create("mov1", "Move");
    model.component("comp1").geom("geom1").feature("mov1").label("Move right prism up PBS2");
    model.component("comp1").geom("geom1").feature("mov1").setIndex("displx", "a+air_domain_length", 0);
    model.component("comp1").geom("geom1").feature("mov1").setIndex("disply", "a+air_domain_length", 0);
    model.component("comp1").geom("geom1").feature("mov1").selection("input").set("rot2");
    model.component("comp1").geom("geom1").create("mov2", "Move");
    model.component("comp1").geom("geom1").feature("mov2").label("Move left prism up PBS2");
    model.component("comp1").geom("geom1").feature("mov2").setIndex("displx", "a+air_domain_length", 0);
    model.component("comp1").geom("geom1").feature("mov2").setIndex("disply", "a+air_domain_length", 0);
    model.component("comp1").geom("geom1").feature("mov2").selection("input").set("rot3");
    model.component("comp1").geom("geom1").create("mov3", "Move");
    model.component("comp1").geom("geom1").feature("mov3").label("Move thin film stack PBS2");
    model.component("comp1").geom("geom1").feature("mov3").setIndex("displx", "a+air_domain_length", 0);
    model.component("comp1").geom("geom1").feature("mov3").setIndex("disply", "a+air_domain_length", 0);
    model.component("comp1").geom("geom1").feature("mov3").selection("input").set("pi1");
    model.component("comp1").geom("geom1").create("r6", "Rectangle");
    model.component("comp1").geom("geom1").feature("r6").label("top BS");
    model.component("comp1").geom("geom1").feature("r6").set("size", new String[]{"a", "a"});
    model.component("comp1").geom("geom1").feature("r6")
         .set("pos", new String[]{"-0.5*a", "0.5*a+air_domain_length"});
    model.component("comp1").geom("geom1").create("r7", "Rectangle");
    model.component("comp1").geom("geom1").feature("r7").label("middle BS");
    model.component("comp1").geom("geom1").feature("r7").set("size", new String[]{"a", "a"});
    model.component("comp1").geom("geom1").feature("r7")
         .set("pos", new String[]{"0.5*a+air_domain_length", "-0.5*a"});
    model.component("comp1").geom("geom1").create("r17", "Rectangle");
    model.component("comp1").geom("geom1").feature("r17").label("bottom BS");
    model.component("comp1").geom("geom1").feature("r17").set("size", new String[]{"a", "a"});
    model.component("comp1").geom("geom1").feature("r17")
         .set("pos", new String[]{"1.5*a+2*air_domain_length", "-1.5*a-air_domain_length"});
    model.component("comp1").geom("geom1").create("ls1", "LineSegment");
    model.component("comp1").geom("geom1").feature("ls1").label("top BS thin film");
    model.component("comp1").geom("geom1").feature("ls1").selection("vertex1").set("r6(1)", 3);
    model.component("comp1").geom("geom1").feature("ls1").selection("vertex2").set("r6(1)", 1);
    model.component("comp1").geom("geom1").create("ls2", "LineSegment");
    model.component("comp1").geom("geom1").feature("ls2").label("middle BS thin film");
    model.component("comp1").geom("geom1").feature("ls2").selection("vertex1").set("r7(1)", 3);
    model.component("comp1").geom("geom1").feature("ls2").selection("vertex2").set("r7(1)", 1);
    model.component("comp1").geom("geom1").create("ls3", "LineSegment");
    model.component("comp1").geom("geom1").feature("ls3").label("bottom BS thin film");
    model.component("comp1").geom("geom1").feature("ls3").selection("vertex1").set("r17(1)", 3);
    model.component("comp1").geom("geom1").feature("ls3").selection("vertex2").set("r17(1)", 1);
    model.component("comp1").geom("geom1").create("r8", "Rectangle");
    model.component("comp1").geom("geom1").feature("r8").label("PML leftmost air");
    model.component("comp1").geom("geom1").feature("r8")
         .set("size", new String[]{"pml_thickness", "air_domain_length"});
    model.component("comp1").geom("geom1").feature("r8").set("pos", new String[]{"-0.5*a-pml_thickness", "0.5*a"});
    model.component("comp1").geom("geom1").create("r9", "Rectangle");
    model.component("comp1").geom("geom1").feature("r9").label("PML rightmost air");
    model.component("comp1").geom("geom1").feature("r9")
         .set("size", new String[]{"pml_thickness", "air_domain_length"});
    model.component("comp1").geom("geom1").feature("r9")
         .set("pos", new String[]{"2.5*a+2*air_domain_length", "-1.5*a-air_domain_length+a"});
    model.component("comp1").geom("geom1").create("r10", "Rectangle");
    model.component("comp1").geom("geom1").feature("r10").label("PML topmost air");
    model.component("comp1").geom("geom1").feature("r10")
         .set("size", new String[]{"air_domain_length", "pml_thickness"});
    model.component("comp1").geom("geom1").feature("r10")
         .set("pos", new String[]{"0.5*a", "1.5*a+air_domain_length"});
    model.component("comp1").geom("geom1").create("r11", "Rectangle");
    model.component("comp1").geom("geom1").feature("r11").label("PML bottommost air");
    model.component("comp1").geom("geom1").feature("r11")
         .set("size", new String[]{"air_domain_length", "pml_thickness"});
    model.component("comp1").geom("geom1").feature("r11")
         .set("pos", new String[]{"1.5*a+air_domain_length", "-1.5*a-air_domain_length-pml_thickness"});
    model.component("comp1").geom("geom1").create("r20", "Rectangle");
    model.component("comp1").geom("geom1").feature("r20").active(false);
    model.component("comp1").geom("geom1").feature("r20").label("PML Prism rightmost top");
    model.component("comp1").geom("geom1").feature("r20")
         .set("size", new String[]{"air_domain_length-2*pml_thickness", "pml_thickness"});
    model.component("comp1").geom("geom1").feature("r20")
         .set("pos", new String[]{"0.5*a+air_domain_length+a+pml_thickness", "-0.5*a-pml_thickness"});
    model.component("comp1").geom("geom1").create("r21", "Rectangle");
    model.component("comp1").geom("geom1").feature("r21").label("PML middle north air");
    model.component("comp1").geom("geom1").feature("r21")
         .set("size", new String[]{"pml_thickness", "air_domain_length-0.5*pml_thickness"});
    model.component("comp1").geom("geom1").feature("r21")
         .set("pos", new String[]{"1.5*a+air_domain_length", "0.5*(a+pml_thickness)"});
    model.component("comp1").geom("geom1").create("r22", "Rectangle");
    model.component("comp1").geom("geom1").feature("r22").label("PML middle west air 1");
    model.component("comp1").geom("geom1").feature("r22")
         .set("size", new String[]{"air_domain_length-pml_thickness", "pml_thickness"});
    model.component("comp1").geom("geom1").feature("r22").set("pos", new String[]{"0.5*a", "-0.5*a-pml_thickness"});
    model.component("comp1").geom("geom1").create("r28", "Rectangle");
    model.component("comp1").geom("geom1").feature("r28").active(false);
    model.component("comp1").geom("geom1").feature("r28").label("PML middle west air west");
    model.component("comp1").geom("geom1").feature("r28")
         .set("size", new String[]{"pml_thickness", "a-pml_thickness"});
    model.component("comp1").geom("geom1").feature("r28")
         .set("pos", new String[]{"0.5*a+air_domain_length+a+air_domain_length+a", "-0.5*a+pml_thickness"});
    model.component("comp1").geom("geom1").create("r24", "Rectangle");
    model.component("comp1").geom("geom1").feature("r24").active(false);
    model.component("comp1").geom("geom1").feature("r24").label("PML middle south air");
    model.component("comp1").geom("geom1").feature("r24")
         .set("size", new String[]{"0.002", "-0.002+air_domain_length"});
    model.component("comp1").geom("geom1").feature("r24")
         .set("pos", new String[]{"-0.002+0.5*a+air_domain_length", "-0.5*a-air_domain_length"});
    model.component("comp1").geom("geom1").create("r12", "Rectangle");
    model.component("comp1").geom("geom1").feature("r12").active(false);
    model.component("comp1").geom("geom1").feature("r12").label("Debugging Prism");
    model.component("comp1").geom("geom1").feature("r12").set("size", new String[]{"a", "a"});
    model.component("comp1").geom("geom1").feature("r12").set("pos", new String[]{"0.25+0.5*a", "0.25+0.5*a"});
    model.component("comp1").geom("geom1").create("r13", "Rectangle");
    model.component("comp1").geom("geom1").feature("r13").label("left_vertical_arm section 2");
    model.component("comp1").geom("geom1").feature("r13").set("size", new String[]{"a", "air_domain_length"});
    model.component("comp1").geom("geom1").feature("r13")
         .set("pos", new String[]{"0.5*a+air_domain_length", "-0.5*a-air_domain_length"});
    model.component("comp1").geom("geom1").create("r14", "Rectangle");
    model.component("comp1").geom("geom1").feature("r14").label("bottom_horizontal_arm  section 2");
    model.component("comp1").geom("geom1").feature("r14").set("size", new String[]{"air_domain_length", "a"});
    model.component("comp1").geom("geom1").feature("r14")
         .set("pos", new String[]{"1.5*a+air_domain_length", "-1.5*a-air_domain_length"});
    model.component("comp1").geom("geom1").create("r15", "Rectangle");
    model.component("comp1").geom("geom1").feature("r15").label("top_horizontal_arm section 2");
    model.component("comp1").geom("geom1").feature("r15").set("size", new String[]{"air_domain_length", "a"});
    model.component("comp1").geom("geom1").feature("r15")
         .set("pos", new String[]{"1.5*a+air_domain_length", "-0.5*a"});
    model.component("comp1").geom("geom1").create("r16", "Rectangle");
    model.component("comp1").geom("geom1").feature("r16").label("right_vertical_arm section 2");
    model.component("comp1").geom("geom1").feature("r16").set("size", new String[]{"a", "air_domain_length"});
    model.component("comp1").geom("geom1").feature("r16")
         .set("pos", new String[]{"1.5*a+2*air_domain_length", "-0.5*a-air_domain_length"});
    model.component("comp1").geom("geom1").create("rot5", "Rotate");
    model.component("comp1").geom("geom1").feature("rot5").label("copy right prism, rotated 1 PBS3");
    model.component("comp1").geom("geom1").feature("rot5").set("keep", true);
    model.component("comp1").geom("geom1").feature("rot5").setIndex("rot", "180", 0);
    model.component("comp1").geom("geom1").feature("rot5").set("contributeto", "csel1");
    model.component("comp1").geom("geom1").feature("rot5").selection("input").set("uni1");
    model.component("comp1").geom("geom1").create("rot6", "Rotate");
    model.component("comp1").geom("geom1").feature("rot6").label("copy left prism 1 PBS3");
    model.component("comp1").geom("geom1").feature("rot6").set("keep", true);
    model.component("comp1").geom("geom1").feature("rot6").set("contributeto", "csel1");
    model.component("comp1").geom("geom1").feature("rot6").selection("input").set("uni1");
    model.component("comp1").geom("geom1").create("pi3", "PartInstance");

    return model;
  }

  public static Model run2(Model model) {
    model.component("comp1").geom("geom1").feature("pi3").label("Thin-Film Stack 3 PBS3");
    model.component("comp1").geom("geom1").feature("pi3").setEntry("inputexpr", "nLayers", "nLayers");
    model.component("comp1").geom("geom1").feature("pi3").setEntry("inputexpr", "dHigh", "dHigh");
    model.component("comp1").geom("geom1").feature("pi3").setEntry("inputexpr", "dLow", "dLow");
    model.component("comp1").geom("geom1").feature("pi3").setEntry("inputexpr", "height", "sqrt(2)*a-dStack");
    model.component("comp1").geom("geom1").feature("pi3").set("rot", -45);
    model.component("comp1").geom("geom1").feature("pi3").set("selkeepnoncontr", false);
    model.component("comp1").geom("geom1").feature("pi3").setEntry("selkeepobj", "pi3_csel1", "on");
    model.component("comp1").geom("geom1").feature("pi3").setEntry("selkeepobj", "pi3_csel2", "on");
    model.component("comp1").geom("geom1").feature("pi3").setEntry("selkeepobj", "pi3_csel3", "on");
    model.component("comp1").geom("geom1").feature("pi3").setEntry("selkeepdom", "pi3_csel1.dom", "on");
    model.component("comp1").geom("geom1").feature("pi3").setEntry("selkeepdom", "pi3_csel2.dom", "on");
    model.component("comp1").geom("geom1").feature("pi3").setEntry("selkeepdom", "pi3_csel3.dom", "on");
    model.component("comp1").geom("geom1").feature("pi3").setEntry("selkeepbnd", "pi3_csel2.bnd", "on");
    model.component("comp1").geom("geom1").feature("pi3").setEntry("selkeepbnd", "pi3_csel3.bnd", "on");
    model.component("comp1").geom("geom1").create("mov4", "Move");
    model.component("comp1").geom("geom1").feature("mov4").label("Move right prism up 1 PBS3");
    model.component("comp1").geom("geom1").feature("mov4").setIndex("displx", "a+air_domain_length", 0);
    model.component("comp1").geom("geom1").feature("mov4").setIndex("disply", "-a-air_domain_length", 0);
    model.component("comp1").geom("geom1").feature("mov4").selection("input").set("rot5");
    model.component("comp1").geom("geom1").create("mov5", "Move");
    model.component("comp1").geom("geom1").feature("mov5").label("Move left prism up 1 PBS3");
    model.component("comp1").geom("geom1").feature("mov5").setIndex("displx", "a+air_domain_length", 0);
    model.component("comp1").geom("geom1").feature("mov5").setIndex("disply", "-a-air_domain_length", 0);
    model.component("comp1").geom("geom1").feature("mov5").selection("input").set("rot6");
    model.component("comp1").geom("geom1").create("mov6", "Move");
    model.component("comp1").geom("geom1").feature("mov6").label("Move thin film stack PBS3");
    model.component("comp1").geom("geom1").feature("mov6").setIndex("displx", "a+air_domain_length", 0);
    model.component("comp1").geom("geom1").feature("mov6").setIndex("disply", "-a-air_domain_length", 0);
    model.component("comp1").geom("geom1").feature("mov6").selection("input").set("pi3");
    model.component("comp1").geom("geom1").create("rot7", "Rotate");
    model.component("comp1").geom("geom1").feature("rot7").label("copy right prism, rotated PBS4");
    model.component("comp1").geom("geom1").feature("rot7").set("keep", true);
    model.component("comp1").geom("geom1").feature("rot7").setIndex("rot", "180", 0);
    model.component("comp1").geom("geom1").feature("rot7").set("contributeto", "csel1");
    model.component("comp1").geom("geom1").feature("rot7").selection("input").set("uni1");
    model.component("comp1").geom("geom1").create("rot8", "Rotate");
    model.component("comp1").geom("geom1").feature("rot8").label("copy left prism PBS4");
    model.component("comp1").geom("geom1").feature("rot8").set("keep", true);
    model.component("comp1").geom("geom1").feature("rot8").set("contributeto", "csel1");
    model.component("comp1").geom("geom1").feature("rot8").selection("input").set("uni1");
    model.component("comp1").geom("geom1").create("pi4", "PartInstance");
    model.component("comp1").geom("geom1").feature("pi4").label("Thin-Film Stack 4");
    model.component("comp1").geom("geom1").feature("pi4").setEntry("inputexpr", "nLayers", "nLayers");
    model.component("comp1").geom("geom1").feature("pi4").setEntry("inputexpr", "dHigh", "dHigh");
    model.component("comp1").geom("geom1").feature("pi4").setEntry("inputexpr", "dLow", "dLow");
    model.component("comp1").geom("geom1").feature("pi4").setEntry("inputexpr", "height", "sqrt(2)*a-dStack");
    model.component("comp1").geom("geom1").feature("pi4").set("rot", -45);
    model.component("comp1").geom("geom1").feature("pi4").set("selkeepnoncontr", false);
    model.component("comp1").geom("geom1").feature("pi4").setEntry("selkeepobj", "pi4_csel1", "on");
    model.component("comp1").geom("geom1").feature("pi4").setEntry("selkeepobj", "pi4_csel2", "on");
    model.component("comp1").geom("geom1").feature("pi4").setEntry("selkeepobj", "pi4_csel3", "on");
    model.component("comp1").geom("geom1").feature("pi4").setEntry("selkeepdom", "pi4_csel1.dom", "on");
    model.component("comp1").geom("geom1").feature("pi4").setEntry("selkeepdom", "pi4_csel2.dom", "on");
    model.component("comp1").geom("geom1").feature("pi4").setEntry("selkeepdom", "pi4_csel3.dom", "on");
    model.component("comp1").geom("geom1").feature("pi4").setEntry("selkeepbnd", "pi4_csel2.bnd", "on");
    model.component("comp1").geom("geom1").feature("pi4").setEntry("selkeepbnd", "pi4_csel3.bnd", "on");
    model.component("comp1").geom("geom1").create("mov7", "Move");
    model.component("comp1").geom("geom1").feature("mov7").label("Move right prism up PBS4");
    model.component("comp1").geom("geom1").feature("mov7").setIndex("displx", "2*(a+air_domain_length)", 0);
    model.component("comp1").geom("geom1").feature("mov7").selection("input").set("rot7");
    model.component("comp1").geom("geom1").create("mov8", "Move");
    model.component("comp1").geom("geom1").feature("mov8").label("Move left prism up PBS4");
    model.component("comp1").geom("geom1").feature("mov8").setIndex("displx", "2*(a+air_domain_length)", 0);
    model.component("comp1").geom("geom1").feature("mov8").selection("input").set("rot8");
    model.component("comp1").geom("geom1").create("mov9", "Move");
    model.component("comp1").geom("geom1").feature("mov9").label("Move thin film stack PBS4");
    model.component("comp1").geom("geom1").feature("mov9").setIndex("displx", "2*(a+air_domain_length)", 0);
    model.component("comp1").geom("geom1").feature("mov9").selection("input").set("pi4");
    model.component("comp1").geom("geom1").create("copy1", "Copy");
    model.component("comp1").geom("geom1").feature("copy1").active(false);
    model.component("comp1").geom("geom1").feature("copy1").label("CZ 2");
    model.component("comp1").geom("geom1").feature("copy1").setIndex("displx", "2*(air_domain_length+a)", 0);
    model.component("comp1").geom("geom1").feature("copy1").setIndex("disply", "-2*(air_domain_length+a)", 0);
    model.component("comp1").geom("geom1").feature("copy1").selection("input")
         .set("mov1", "mov2", "mov3", "mov4", "mov5", "mov6", "mov7", "mov8", "mov9", "pi2", "r13", "r14", "r15", "r16", "r17", "r2", "r21", "r3", "r4", "r5", "r7", "r9", "rot1", "uni1");
    model.component("comp1").geom("geom1").create("r23", "Rectangle");
    model.component("comp1").geom("geom1").feature("r23").active(false);
    model.component("comp1").geom("geom1").feature("r23").label("univeral qubit set air domain");
    model.component("comp1").geom("geom1").feature("r23").set("size", new String[]{"3*a+2*air_domain_length", "a"});
    model.component("comp1").geom("geom1").feature("r23")
         .set("pos", new String[]{"-0.5*a", "-3.5*a-air_domain_length"});
    model.component("comp1").geom("geom1").create("r25", "Rectangle");
    model.component("comp1").geom("geom1").feature("r25").active(false);
    model.component("comp1").geom("geom1").feature("r25").label("Quarter Waveplate 1");
    model.component("comp1").geom("geom1").feature("r25").set("size", new String[]{"17.1[um]", "a"});
    model.component("comp1").geom("geom1").feature("r25").set("pos", new String[]{"0", "-3.5*a-air_domain_length"});
    model.component("comp1").geom("geom1").create("r27", "Rectangle");
    model.component("comp1").geom("geom1").feature("r27").active(false);
    model.component("comp1").geom("geom1").feature("r27").label("Half Waveplate");
    model.component("comp1").geom("geom1").feature("r27").set("size", new String[]{"34.2[um]", "a"});
    model.component("comp1").geom("geom1").feature("r27")
         .set("pos", new String[]{"air_domain_length", "-3.5*a-air_domain_length"});
    model.component("comp1").geom("geom1").create("r26", "Rectangle");
    model.component("comp1").geom("geom1").feature("r26").active(false);
    model.component("comp1").geom("geom1").feature("r26").label("Quarter Waveplate 2");
    model.component("comp1").geom("geom1").feature("r26").set("size", new String[]{"17.1[um]", "a"});
    model.component("comp1").geom("geom1").feature("r26")
         .set("pos", new String[]{"2*air_domain_length", "-3.5*a-air_domain_length"});
    model.component("comp1").geom("geom1").create("ls5", "LineSegment");
    model.component("comp1").geom("geom1").feature("ls5").active(false);
    model.component("comp1").geom("geom1").feature("ls5").selection("vertex1").set("copy1(15)", 1);
    model.component("comp1").geom("geom1").feature("ls5").selection("vertex2").set("copy1(15)", 3);
    model.component("comp1").geom("geom1").create("r29", "Rectangle");
    model.component("comp1").geom("geom1").feature("r29").active(false);
    model.component("comp1").geom("geom1").feature("r29").label("right_vertical_arm section 2.1");
    model.component("comp1").geom("geom1").feature("r29").set("size", new String[]{"a", "a"});
    model.component("comp1").geom("geom1").feature("r29")
         .set("pos", new String[]{"0.5*a+air_domain_length+a+air_domain_length+a", "-0.5*a"});
    model.component("comp1").geom("geom1").feature("fin").set("repairtoltype", "absolute");
    model.component("comp1").geom("geom1").feature("fin").set("absrepairtol", 1.5E-5);
    model.component("comp1").geom("geom1").create("mce1", "MeshControlEdges");
    model.component("comp1").geom("geom1").feature("mce1").selection("input")
         .set("fin(1)", 8, 29, 30, 33, 35, 37, 65, 79, 110, 111, 112, 113, 118, 120, 122, 124, 126, 127, 173, 189, 190, 193, 195, 197);
    model.component("comp1").geom("geom1").run();
    model.component("comp1").geom("geom1").run("r29");

    model.component("comp1").selection().create("disk1", "Disk");
    model.component("comp1").selection().create("disk2", "Disk");
    model.component("comp1").selection().create("sel1", "Explicit");
    model.component("comp1").selection("sel1").geom("geom1", 2, 1, new String[]{"exterior"});
    model.component("comp1").selection("sel1").all();
    model.component("comp1").selection().create("int1", "Intersection");
    model.component("comp1").selection("int1").set("entitydim", 1);
    model.component("comp1").selection().create("box1", "Box");
    model.component("comp1").selection("box1").set("entitydim", 1);
    model.component("comp1").selection().create("box2", "Box");
    model.component("comp1").selection("box2").set("entitydim", 1);
    model.component("comp1").selection().create("box3", "Box");
    model.component("comp1").selection("box3").set("entitydim", 1);
    model.component("comp1").selection().create("box4", "Box");
    model.component("comp1").selection("box4").set("entitydim", 1);
    model.component("comp1").selection().create("box5", "Box");
    model.component("comp1").selection("box5").set("entitydim", 1);
    model.component("comp1").selection().create("sel2", "Explicit");
    model.component("comp1").selection("disk1").label("First Prism");
    model.component("comp1").selection("disk1").set("posx", "-0.25*a");
    model.component("comp1").selection("disk1").set("posy", "0.25*a");
    model.component("comp1").selection("disk2").label("Second Prism");
    model.component("comp1").selection("disk2").set("posx", "0.25*a");
    model.component("comp1").selection("disk2").set("posy", "-0.25*a");
    model.component("comp1").selection("sel1").label("Exterior Boundaries");
    model.component("comp1").selection("int1").label("Thin-Film Stack Exterior Boundaries");
    model.component("comp1").selection("int1").set("input", new String[]{"sel1", "geom1_pi1_csel3_bnd"});
    model.component("comp1").selection("box1").label("Left Boundary");
    model.component("comp1").selection("box1").set("inputent", "selections");
    model.component("comp1").selection("box1").set("input", new String[]{"sel1"});
    model.component("comp1").selection("box1").set("xmax", "-0.5*(a-dHigh/sqrt(2))");
    model.component("comp1").selection("box1").set("ymin", "-0.5*a");
    model.component("comp1").selection("box1").set("ymax", "0.5*a");
    model.component("comp1").selection("box1").set("condition", "allvertices");
    model.component("comp1").selection("box2").label("Top Boundary");
    model.component("comp1").selection("box2").set("inputent", "selections");
    model.component("comp1").selection("box2").set("input", new String[]{"sel1"});
    model.component("comp1").selection("box2").set("xmin", "-0.5*a");
    model.component("comp1").selection("box2").set("xmax", "0.5*a");
    model.component("comp1").selection("box2").set("ymin", "0.5*(a-dHigh/sqrt(2))");
    model.component("comp1").selection("box2").set("condition", "allvertices");
    model.component("comp1").selection("box3").label("Right Boundary");
    model.component("comp1").selection("box3").set("inputent", "selections");
    model.component("comp1").selection("box3").set("input", new String[]{"sel1"});
    model.component("comp1").selection("box3").set("xmin", "0.5*(a-dHigh/sqrt(2))");
    model.component("comp1").selection("box3").set("ymin", "-0.5*a");
    model.component("comp1").selection("box3").set("ymax", "0.5*a");
    model.component("comp1").selection("box3").set("condition", "allvertices");
    model.component("comp1").selection("box4").label("Bottom Boundary");
    model.component("comp1").selection("box4").set("inputent", "selections");
    model.component("comp1").selection("box4").set("input", new String[]{"sel1"});
    model.component("comp1").selection("box4").set("xmin", "-0.5*a");
    model.component("comp1").selection("box4").set("xmax", "0.5*a");
    model.component("comp1").selection("box4").set("ymax", "-0.5*(a-dHigh/sqrt(2))");
    model.component("comp1").selection("box4").set("condition", "allvertices");
    model.component("comp1").selection("box5").label("Top-Right Thin-Film Exterior Boundaries");
    model.component("comp1").selection("box5").set("xmin", 0);
    model.component("comp1").selection("box5").set("ymin", 0);
    model.component("comp1").selection("box5").set("condition", "inside");
    model.component("comp1").selection("sel2").label("2");

    model.component("comp1").variable().create("var13");
    model.component("comp1").variable("var13").set("nP", "aveop1(ewbe.nxx)", "Refractive index, prisms");
    model.component("comp1").variable("var13")
         .set("nH", "aveop2(ewbe.nxx)", "Refractive index, high-index material");
    model.component("comp1").variable("var13").set("nL", "aveop3(ewbe.nxx)", "Refractive index, low-index material");
    model.component("comp1").variable("var13").set("kt", "k0*nP/sqrt(2)", "Tangential wave vector component");
    model.component("comp1").variable("var13")
         .set("kHn", "k0*sqrt(nH^2-nP^2/2)", "Normal wave vector component, high-index material");
    model.component("comp1").variable("var13")
         .set("kLn", "k0*sqrt(nL^2-nP^2/2)", "Normal wave vector component, low-index material");
    model.component("comp1").variable().create("var2");
    model.component("comp1").variable("var2")
         .set("k1Prismx", "k0*nP", "Wave vector in prisms, first wave, x-component");
    model.component("comp1").variable("var2")
         .set("k1Prismy", "0[1/m]", "Wave vector in prisms, first wave, y-component");
    model.component("comp1").variable("var2")
         .set("k1Highx", "(kt+kHn)/sqrt(2)", "Wave vector in high-index materials, first wave, x-component");
    model.component("comp1").variable("var2")
         .set("k1Highy", "(kt-kHn)/sqrt(2)", "Wave vector in high-index materials, first wave, y-component");
    model.component("comp1").variable("var2")
         .set("k1Lowx", "(kt+kLn)/sqrt(2)", "Wave vector in low-index materials, first wave, x-component");
    model.component("comp1").variable("var2")
         .set("k1Lowy", "(kt-kLn)/sqrt(2)", "Wave vector in low-index materials, first wave, y-component");
    model.component("comp1").variable("var2")
         .set("k2Prismx", "k1Prismy", "Wave vector in prisms, second wave, x-component");
    model.component("comp1").variable("var2")
         .set("k2Prismy", "k1Prismx", "Wave vector in prisms, second wave, y-component");
    model.component("comp1").variable("var2")
         .set("k2Highx", "k1Highy", "Wave vector in high-index materials, second wave, x-component");
    model.component("comp1").variable("var2")
         .set("k2Highy", "k1Highx", "Wave vector in high-index materials, second wave, y-component");
    model.component("comp1").variable("var2")
         .set("k2Lowx", "k1Lowy", "Wave vector in low-index materials, second wave, x-component");
    model.component("comp1").variable("var2")
         .set("k2Lowy", "k1Lowx", "Wave vector in low-index materials, second wave, y-component");
    model.component("comp1").variable("var2")
         .set("k1Airx", "k0*n_air", "Wave vector for horizontal wave in air domain");
    model.component("comp1").variable("var2")
         .set("k1Airy", "0[1/m]", "a horizontal wave should have 0 vertical component");
    model.component("comp1").variable("var2")
         .set("k2Airx", "0[1/m]", "a vertical wave should have 0 horizontal component");
    model.component("comp1").variable("var2")
         .set("k2Airy", "k0*n_air", "Wave vector for vertical wave in vertical domain");
    model.component("comp1").variable().create("var3");
    model.component("comp1").variable("var3").set("phi1", "k1Prismx*x+k1Prismy*y+phi100", "Phase for first wave");
    model.component("comp1").variable("var3").set("phi2", "k2Prismx*x+k2Prismy*y+phi200", "Phase for second wave");
    model.component("comp1").variable("var3").selection().geom("geom1", 2);
    model.component("comp1").variable("var3").selection().set(2, 6, 18, 21, 22, 24, 44, 47);
    model.component("comp1").variable().create("var14");
    model.component("comp1").variable("var14").set("phi1", "k1Highx*x+k1Highy*y+phi110", "Phase for first wave");
    model.component("comp1").variable("var14").set("phi2", "k2Highx*x+k2Highy*y+phi210", "Phase for second wave");
    model.component("comp1").variable("var14").selection().geom("geom1", 2);
    model.component("comp1").variable("var14").selection().set(3, 19, 25, 48);
    model.component("comp1").variable().create("var15");
    model.component("comp1").variable("var15").set("phi1", "k1Lowx*x+k1Lowy*y+phi120", "Phase for first wave");
    model.component("comp1").variable("var15").set("phi2", "k2Lowx*x+k2Lowy*y+phi220", "Phase for second wave");
    model.component("comp1").variable("var15").selection().geom("geom1", 2);
    model.component("comp1").variable("var15").selection().set(7, 26, 27, 49);
    model.component("comp1").variable().create("var16");
    model.component("comp1").variable("var16").set("phi1", "k1Highx*x+k1Highy*y+phi130", "Phase for first wave");
    model.component("comp1").variable("var16").set("phi2", "k2Highx*x+k2Highy*y+phi230", "Phase for second wave");
    model.component("comp1").variable("var16").selection().geom("geom1", 2);
    model.component("comp1").variable("var16").selection().set(8, 28, 29, 50);
    model.component("comp1").variable().create("var17");
    model.component("comp1").variable("var17").set("phi1", "k1Lowx*x+k1Lowy*y", "Phase for first wave");
    model.component("comp1").variable("var17").set("phi2", "k2Lowx*x+k2Lowy*y", "Phase for second wave");
    model.component("comp1").variable("var17").selection().geom("geom1", 2);
    model.component("comp1").variable("var17").selection().set(9, 30, 31, 51);
    model.component("comp1").variable().create("var18");
    model.component("comp1").variable("var18").set("phi1", "k1Highx*x+k1Highy*y+phi150", "Phase for first wave");
    model.component("comp1").variable("var18").set("phi2", "k2Highx*x+k2Highy*y+phi250", "Phase for second wave");
    model.component("comp1").variable("var18").selection().geom("geom1", 2);
    model.component("comp1").variable("var18").selection().set(10, 32, 33, 52);
    model.component("comp1").variable().create("var19");
    model.component("comp1").variable("var19").set("phi1", "k1Lowx*x+k1Lowy*y+phi160", "Phase for first wave");
    model.component("comp1").variable("var19").set("phi2", "k2Lowx*x+k2Lowy*y+phi260", "Phase for second wave");
    model.component("comp1").variable("var19").selection().geom("geom1", 2);
    model.component("comp1").variable("var19").selection().set(11, 34, 35, 53);
    model.component("comp1").variable().create("var20");
    model.component("comp1").variable("var20").set("phi1", "k1Highx*x+k1Highy*y+phi170", "Phase for first wave");
    model.component("comp1").variable("var20").set("phi2", "k2Highx*x+k2Highy*y+phi270", "Phase for second wave");
    model.component("comp1").variable("var20").selection().geom("geom1", 2);
    model.component("comp1").variable("var20").selection().set(12, 36, 37, 54);
    model.component("comp1").variable().create("var21");
    model.component("comp1").variable("var21").set("phi1", "k1Prismx*x+k1Prismy*y+phi180", "Phase for first wave");
    model.component("comp1").variable("var21").set("phi2", "k2Prismx*x+k2Prismy*y+phi280", "Phase for second wave");
    model.component("comp1").variable("var21").selection().geom("geom1", 2);
    model.component("comp1").variable("var21").selection().set(5, 13, 38, 39, 45, 55);
    model.component("comp1").variable().create("var24");
    model.component("comp1").variable("var24")
         .set("phi1", "k1Prismx*pml1.x+k1Prismy*pml1.y+phi180", "Phase for first wave");
    model.component("comp1").variable("var24")
         .set("phi2", "k2Prismx*pml1.x+k2Prismy*pml1.y+phi280", "Phase for second wave");
    model.component("comp1").variable("var24").selection().geom("geom1", 2);
    model.component("comp1").variable().create("var22");
    model.component("comp1").variable("var22").set("phi1", "k1Airx*x+k1Airy*y+phiAirx", "Phase for first wave");
    model.component("comp1").variable("var22").set("phi2", "k2Airx*x+k2Airy*y+phiAiry", "Phase for second wave");
    model.component("comp1").variable("var22").selection().geom("geom1", 2);
    model.component("comp1").variable("var22").selection().set(4, 15, 16, 20, 23, 41, 42, 46);
    model.component("comp1").variable().create("var23");
    model.component("comp1").variable("var23")
         .set("phi1", "k1Airx*pml1.x+k1Airy*pml1.y+phiAirx", "Phase for first wave");
    model.component("comp1").variable("var23")
         .set("phi2", "k2Airx*pml1.x+k2Airy*pml1.y+phiAiry", "Phase for second wave");
    model.component("comp1").variable("var23").selection().geom("geom1", 2);
    model.component("comp1").variable("var23").selection().set(1, 14, 17, 40, 43, 56);
    model.component("comp1").variable().create("var1");
    model.component("comp1").variable("var1").set("x0", "-dStack/sqrt(2)", "Boundary 0");
    model.component("comp1").variable("var1").set("x1", "x0+sqrt(2)*dHigh", "Boundary 1");
    model.component("comp1").variable("var1").set("x2", "x1+sqrt(2)*dLow", "Boundary 2");
    model.component("comp1").variable("var1").set("x3", "x2+sqrt(2)*dHigh", "Boundary 3");
    model.component("comp1").variable("var1").set("x4", "x3+sqrt(2)*dLow", "Boundary 4");
    model.component("comp1").variable("var1").set("x5", "x4+sqrt(2)*dHigh", "Boundary 5");
    model.component("comp1").variable("var1").set("x6", "x5+sqrt(2)*dLow", "Boundary 6");
    model.component("comp1").variable("var1").set("x7", "x6+sqrt(2)*dHigh", "Boundary 7");
    model.component("comp1").variable("var1").set("x8", "a/2", "Interface of right prism and air domain");
    model.component("comp1").variable().create("var12");
    model.component("comp1").variable("var12").set("Pin", "-intop1(ewbe.nPoav)", "Input beam power");
    model.component("comp1").variable("var12")
         .set("Ptop", "intop2(ewbe.Poavx*nx + ewbe.Poavy*ny)", "Top beam power");
    model.component("comp1").variable("var12")
         .set("Pright", "intop3(ewbe.Poavx*nx + ewbe.Poavy*ny)", "Right beam power");
    model.component("comp1").variable("var12")
         .set("Pbottom", "intop4(ewbe.Poavx*nx + ewbe.Poavy*ny)", "Bottom beam power");
    model.component("comp1").variable("var12").set("BS1_bin", "intop5(ewbe.nPoav)");
    model.component("comp1").variable("var12").set("BS3_bin", "intop6(ewbe.nPoav)");
    model.component("comp1").variable("var12").set("C_o", "intop7(ewbe.nPoav)");
    model.component("comp1").variable("var12").set("T_o", "intop8(ewbe.nPoav)");
    model.component("comp1").variable("var12").set("Top_power_fraction_PBS1", "Ptop/Pin");
    model.component("comp1").variable("var12").set("Right_power_fraction_PBS1", "Pright/Pin");
    model.component("comp1").variable("var12").set("BS1_attenuation_factor", "BS1_bin/Pin");
    model.component("comp1").variable("var12").set("BS3_attenuation_factor", "BS3_bin/Pin");
    model.component("comp1").variable("var12").set("C_o_fraction", "C_o/(2*Pin)");
    model.component("comp1").variable("var12").set("T_o_fraction", "T_o/(2*Pin)");
    model.component("comp1").variable("var12").set("BS3_output_beam", "intop9(ewbe.Poavx*nx + ewbe.Poavy*ny)");
    model.component("comp1").variable("var12").set("Pkept", "(BS1_bin+BS3_bin+C_o+T_o)/(2*Pin)");

    model.view().create("view3", 3);
    model.view().create("view4", 3);
    model.view().create("view5", 3);
    model.view().create("view6", 2);

    model.component("comp1").material().create("mat0", "Common");
    model.component("comp1").material().create("mat1", "Common");
    model.component("comp1").material().create("mat2", "Common");
    model.component("comp1").material().create("mat3", "Common");
    model.material().create("mat4", "Common", "");
    model.material().create("mat5", "Common", "");
    model.material().create("mat6", "Common", "");
    model.component("comp1").material().create("matlnk1", "Link");
    model.component("comp1").material().create("matlnk2", "Link");
    model.component("comp1").material().create("matlnk3", "Link");
    model.component("comp1").material().create("mat7", "Common");
    model.component("comp1").material().create("mat8", "Common");
    model.component("comp1").material().create("mat9", "Common");
    model.component("comp1").material("mat0").selection().set();
    model.component("comp1").material("mat0").propertyGroup()
         .create("RefractiveIndex", "RefractiveIndex", "Refractive index");
    model.component("comp1").material("mat0").selection()
         .set(1, 4, 5, 6, 14, 15, 16, 17, 20, 21, 22, 23, 40, 41, 42, 43, 44, 45, 46, 56);
    model.component("comp1").material("mat1").selection().set(2, 5, 6, 13, 18, 21, 22, 24, 38, 39, 44, 45, 47, 55);
    model.component("comp1").material("mat1").propertyGroup()
         .create("RefractiveIndex", "RefractiveIndex", "Refractive index");
    model.component("comp1").material("mat2").selection()
         .set(3, 8, 10, 12, 19, 25, 28, 29, 32, 33, 36, 37, 48, 50, 52, 54);
    model.component("comp1").material("mat2").propertyGroup()
         .create("RefractiveIndex", "RefractiveIndex", "Refractive index");
    model.component("comp1").material("mat3").selection().set(7, 9, 11, 26, 27, 30, 31, 34, 35, 49, 51, 53);
    model.component("comp1").material("mat3").propertyGroup()
         .create("RefractiveIndex", "RefractiveIndex", "Refractive index");
    model.material("mat4").propertyGroup()
         .create("DispersionModelSellmeierStandard", "DispersionModelSellmeierStandard", "Sellmeier");
    model.material("mat4").propertyGroup()
         .create("ThermoOpticDispersionModelSchott", "ThermoOpticDispersionModelSchott", "Schott thermo-optic");
    model.material("mat4").propertyGroup().create("Enu", "Enu", "Young's modulus and Poisson's ratio");
    model.material("mat4").propertyGroup()
         .create("InternalTransmittance10", "InternalTransmittance10", "Internal transmittance, 10\u00a0mm sample thickness");
    model.material("mat4").propertyGroup("InternalTransmittance10").func().create("int1", "Interpolation");
    model.material("mat4").propertyGroup()
         .create("InternalTransmittance25", "InternalTransmittance25", "Internal transmittance, 25\u00a0mm sample thickness");
    model.material("mat4").propertyGroup("InternalTransmittance25").func().create("int1", "Interpolation");
    model.material("mat5").propertyGroup().create("RefractiveIndex", "RefractiveIndex", "Refractive index");
    model.material("mat5").propertyGroup("RefractiveIndex").func().create("an1", "Analytic");
    model.material("mat6").propertyGroup().create("RefractiveIndex", "RefractiveIndex", "Refractive index");
    model.material("mat6").propertyGroup("RefractiveIndex").func().create("an1", "Analytic");
    model.component("comp1").material("matlnk1").selection().named("geom1_csel1_dom");
    model.component("comp1").material("matlnk2").selection().named("geom1_pi1_csel1_dom");
    model.component("comp1").material("matlnk3").selection().named("geom1_pi1_csel2_dom");
    model.component("comp1").material("mat7").propertyGroup()
         .create("RefractiveIndex", "RefractiveIndex", "Refractive index");
    model.component("comp1").material("mat7").propertyGroup("RefractiveIndex").func()
         .create("int1", "Interpolation");
    model.component("comp1").material("mat7").propertyGroup("RefractiveIndex").func()
         .create("int2", "Interpolation");
    model.component("comp1").material("mat8").info().create("Composition");
    model.component("comp1").material("mat8").propertyGroup()
         .create("RefractiveIndex", "RefractiveIndex", "Refractive index");
    model.component("comp1").material("mat8").propertyGroup("RefractiveIndex").func()
         .create("RIWL_tested_at_180C_1", "Piecewise");
    model.component("comp1").material("mat9").info().create("Composition");
    model.component("comp1").material("mat9").propertyGroup()
         .create("RefractiveIndex", "RefractiveIndex", "Refractive index");
    model.component("comp1").material("mat9").propertyGroup("RefractiveIndex").func()
         .create("RIWL_tested_at_30C_4", "Piecewise");

    model.component("comp1").cpl().create("intop1", "Integration");
    model.component("comp1").cpl().create("intop2", "Integration");
    model.component("comp1").cpl().create("intop3", "Integration");
    model.component("comp1").cpl().create("intop4", "Integration");
    model.component("comp1").cpl().create("intop5", "Integration");
    model.component("comp1").cpl().create("intop6", "Integration");
    model.component("comp1").cpl().create("intop7", "Integration");
    model.component("comp1").cpl().create("intop8", "Integration");
    model.component("comp1").cpl().create("intop9", "Integration");
    model.component("comp1").cpl().create("aveop1", "Average");
    model.component("comp1").cpl().create("aveop2", "Average");
    model.component("comp1").cpl().create("aveop3", "Average");
    model.component("comp1").cpl("intop1").selection().geom("geom1", 1);
    model.component("comp1").cpl("intop1").selection().set(4);
    model.component("comp1").cpl("intop2").selection().geom("geom1", 1);
    model.component("comp1").cpl("intop2").selection().set(8);
    model.component("comp1").cpl("intop3").selection().geom("geom1", 1);
    model.component("comp1").cpl("intop3").selection().set(37);
    model.component("comp1").cpl("intop4").selection().geom("geom1", 1);
    model.component("comp1").cpl("intop4").selection().set(25);

    return model;
  }

  public static Model run3(Model model) {
    model.component("comp1").cpl("intop5").selection().geom("geom1", 1);
    model.component("comp1").cpl("intop5").selection().set(12);
    model.component("comp1").cpl("intop6").selection().geom("geom1", 1);
    model.component("comp1").cpl("intop6").selection().set(158);
    model.component("comp1").cpl("intop7").selection().geom("geom1", 1);
    model.component("comp1").cpl("intop7").selection().set(65);
    model.component("comp1").cpl("intop8").selection().geom("geom1", 1);
    model.component("comp1").cpl("intop8").selection().set(161);
    model.component("comp1").cpl("intop9").selection().geom("geom1", 1);
    model.component("comp1").cpl("intop9").selection().set(130);
    model.component("comp1").cpl("aveop1").selection().named("geom1_csel1_dom");
    model.component("comp1").cpl("aveop2").selection().set(3, 19, 25, 36, 48, 54);
    model.component("comp1").cpl("aveop3").selection().set(27);

    model.component("comp1").coordSystem().create("pml1", "PML");
    model.component("comp1").coordSystem("pml1").selection().set(1, 14, 17, 40, 43, 56);

    model.component("comp1").physics().create("ewbe", "ElectromagneticWavesBeamEnvelopes", "geom1");
    model.component("comp1").physics("ewbe").selection().set();
    model.component("comp1").physics("ewbe").create("mbc1", "MatchedBoundaryCondition", 1);
    model.component("comp1").physics("ewbe").feature("mbc1").selection().set(4);
    model.component("comp1").physics("ewbe").create("mbc2", "MatchedBoundaryCondition", 1);
    model.component("comp1").physics("ewbe").feature("mbc2").selection()
         .set(7, 10, 12, 40, 49, 52, 57, 58, 65, 92, 93, 113, 114, 118, 120, 129, 136, 159);
    model.component("comp1").physics("ewbe").create("mbc3", "MatchedBoundaryCondition", 1);
    model.component("comp1").physics("ewbe").feature("mbc3").selection()
         .set(9, 25, 37, 38, 41, 42, 43, 45, 48, 54, 63, 95, 112, 115, 116, 117, 119, 121, 122, 127, 158, 161);
    model.component("comp1").physics("ewbe").create("mbc4", "MatchedBoundaryCondition", 1);
    model.component("comp1").physics("ewbe").feature("mbc4").selection().set(90);
    model.component("comp1").physics("ewbe").create("sctr1", "Scattering", 1);
    model.component("comp1").physics("ewbe").feature("sctr1").selection()
         .set(5, 9, 13, 15, 17, 19, 21, 23, 27, 28, 29, 30, 31, 32, 33, 34, 39, 50, 55, 59, 60, 62, 66, 68, 70, 72, 74, 76, 78, 80, 82, 84, 86, 88, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 113, 127, 131, 132, 134, 137, 139, 141, 143, 145, 147, 151, 152, 153, 154, 155, 156, 157, 161);
    model.component("comp1").physics("ewbe").create("trans1", "TransitionBoundaryCondition", 1);
    model.component("comp1").physics("ewbe").feature("trans1").selection()
         .set(8, 10, 37, 42, 53, 54, 55, 58, 59, 60, 61, 65, 92, 111, 116, 126, 130, 131, 133, 136, 149, 161);
    model.component("comp1").physics("ewbe").create("trans2", "TransitionBoundaryCondition", 1);
    model.component("comp1").physics("ewbe").feature("trans2").selection().set(11, 56, 128);
    model.component("comp1").physics().create("ge", "GlobalEquations", "geom1");
    model.component("comp1").physics().create("ge1", "GlobalEquations", "geom1");

    model.component("comp1").mesh("mesh1").create("edg1", "Edge");
    model.component("comp1").mesh("mesh1").create("map3", "Map");
    model.component("comp1").mesh("mesh1").create("map2", "Map");
    model.component("comp1").mesh("mesh1").create("ftri1", "FreeTri");
    model.component("comp1").mesh("mesh1").feature("edg1").selection().set(6, 26, 51, 63, 91, 93, 135, 150);
    model.component("comp1").mesh("mesh1").feature("map3").selection().geom("geom1", 2);
    model.component("comp1").mesh("mesh1").feature("map3").selection()
         .set(3, 7, 8, 9, 10, 11, 12, 19, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 48, 49, 50, 51, 52, 53, 54);
    model.component("comp1").mesh("mesh1").feature("map3").create("dis1", "Distribution");
    model.component("comp1").mesh("mesh1").feature("map2").selection().geom("geom1", 2);
    model.component("comp1").mesh("mesh1").feature("map2").selection()
         .set(1, 2, 4, 13, 14, 15, 16, 17, 18, 20, 23, 24, 38, 39, 40, 41, 42, 43, 46, 47, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72);
    model.component("comp1").mesh("mesh1").feature("map2").create("dis1", "Distribution");
    model.component("comp1").mesh("mesh1").feature("map2").create("dis2", "Distribution");
    model.component("comp1").mesh("mesh1").feature("map2").feature("dis1").selection()
         .set(2, 3, 35, 44, 123, 125, 160, 162);
    model.component("comp1").mesh("mesh1").feature("map2").feature("dis2").selection().set(184);
    model.component("comp1").mesh("mesh1").feature("ftri1").selection().geom("geom1", 2);
    model.component("comp1").mesh("mesh1").feature("ftri1").selection().set(5, 6, 21, 22, 44, 45);
    model.component("comp1").mesh("mesh1").feature("ftri1").create("size1", "Size");

    model.result().table("tbl1").active(false);
    model.result().table("tbl1").comments("Surface Average 1 (ewbe.nxx)");
    model.result().table("tbl2").active(false);
    model.result().table("tbl2").comments("Global Evaluation 1");
    model.result().table("tbl5").comments("Line Integration 1");
    model.result().table("tbl7").comments("Global Evaluation 1");
    model.result().table("tbl8").label("Table 8 (Lambda and ADL)");
    model.result().table("evl2").label("Evaluation 2D");
    model.result().table("evl2").comments("Interactive 2D values");

    model.component("comp1").variable("var13").label("Refractive Indices and Wave Vector Components");
    model.component("comp1").variable("var2").label("Wave Vector Components");
    model.component("comp1").variable("var3").label("Phases in First Prism");
    model.component("comp1").variable("var14").label("Phases for high-index layer1");
    model.component("comp1").variable("var15").label("Phases for low-index layer2");
    model.component("comp1").variable("var16").label("Phases for high-index layer3");
    model.component("comp1").variable("var17").label("Phases for low-index layer4");
    model.component("comp1").variable("var18").label("Phases for high-index layer5");
    model.component("comp1").variable("var19").label("Phases for low-index layer6");
    model.component("comp1").variable("var20").label("Phases for high-index layer7");
    model.component("comp1").variable("var21").label("Phases for second prism");
    model.component("comp1").variable("var24").label("Phases for prism PML");
    model.component("comp1").variable("var22").label("Phases post prism");
    model.component("comp1").variable("var23").label("Phases post prism PML");
    model.component("comp1").variable("var1").label("Positions");
    model.component("comp1").variable("var12").label("Power");

    model.component("comp1").view("view1").axis().set("xmin", -42.565162658691406);
    model.component("comp1").view("view1").axis().set("xmax", 61.575775146484375);
    model.component("comp1").view("view1").axis().set("ymin", -22.49163055419922);
    model.component("comp1").view("view1").axis().set("ymax", 22.49163055419922);
    model.view("view2").axis().set("xmin", -4.851190624322044E-6);
    model.view("view2").axis().set("xmax", 4.851190624322044E-6);
    model.view("view2").axis().set("ymin", -4.245992840878898E-6);
    model.view("view2").axis().set("ymax", 4.245992840878898E-6);
    model.view("view3").set("showgrid", false);
    model.view("view6").axis().set("xmin", 731.7562866210938);
    model.view("view6").axis().set("xmax", 868.2437744140625);
    model.view("view6").axis().set("ymin", 4.408940412758966E-8);
    model.view("view6").axis().set("ymax", 6.51792220196512E-8);
    model.view("view6").axis().set("viewscaletype", "automatic");

    model.material().label("Dummy Component for Material Property Evaluation");
    model.component("comp1").material("mat0").label("Aadi's Simple Air");
    model.component("comp1").material("mat0").propertyGroup("RefractiveIndex")
         .set("n", new String[]{"n_air", "0", "0", "0", "n_air", "0", "0", "0", "n_air"});
    model.component("comp1").material("mat1").label("Prisms");
    model.component("comp1").material("mat1").propertyGroup("RefractiveIndex")
         .set("n", new String[]{"nPrism", "0", "0", "0", "nPrism", "0", "0", "0", "nPrism"});
    model.component("comp1").material("mat2").label("High-Index Material");
    model.component("comp1").material("mat2").propertyGroup("RefractiveIndex")
         .set("n", new String[]{"nHigh", "0", "0", "0", "nHigh", "0", "0", "0", "nHigh"});
    model.component("comp1").material("mat3").label("Low-Index Material");
    model.component("comp1").material("mat3").propertyGroup("RefractiveIndex")
         .set("n", new String[]{"nLow", "0", "0", "0", "nLow", "0", "0", "0", "nLow"});
    model.material("mat4").label("Schott N-SF5 Glass");
    model.material("mat4").propertyGroup("def").set("density", "2.86[g/cm^3]");
    model.material("mat4").propertyGroup("def").set("heatcapacity", "0.77[J/(g*K)]");
    model.material("mat4").propertyGroup("def")
         .set("thermalconductivity", new String[]{"1.0[W/(m*K)]", "0", "0", "0", "1.0[W/(m*K)]", "0", "0", "0", "1.0[W/(m*K)]"});
    model.material("mat4").propertyGroup("def")
         .set("thermalexpansioncoefficient", new String[]{"7.94E-6[1/K]", "0", "0", "0", "7.94E-6[1/K]", "0", "0", "0", "7.94E-6[1/K]"});
    model.material("mat4").propertyGroup("DispersionModelSellmeierStandard")
         .set("ODsma", new String[]{"1.52481889E+00", "1.87085527E-01", "1.42729015E+00", "1.12547560E-02", "5.88995392E-02", "1.29141675E+02"});
    model.material("mat4").propertyGroup("DispersionModelSellmeierStandard").set("Trefsma", "22[degC]");
    model.material("mat4").propertyGroup("DispersionModelSellmeierStandard").set("Prefsma", "1[atm]");
    model.material("mat4").propertyGroup("DispersionModelSellmeierStandard").addInput("frequency");
    model.material("mat4").propertyGroup("ThermoOpticDispersionModelSchott")
         .set("TOsco", new String[]{"-2.51E-7", "1.07E-8", "-2.4E-11", "7.85E-7", "1.15E-9", "0.278"});
    model.material("mat4").propertyGroup("ThermoOpticDispersionModelSchott").set("Trefsco", "20[degC]");
    model.material("mat4").propertyGroup("Enu").set("E", "87.0[GPa]");
    model.material("mat4").propertyGroup("Enu").set("nu", "0.237");
    model.material("mat4").propertyGroup("InternalTransmittance10").func("int1").set("funcname", "taui10");
    model.material("mat4").propertyGroup("InternalTransmittance10").func("int1")
         .set("table", new String[][]{{"2500", "0.758"}, 
         {"2325", "0.831"}, 
         {"1970", "0.95"}, 
         {"1530", "0.99"}, 
         {"1060", "0.998"}, 
         {"700", "0.996"}, 
         {"660", "0.995"}, 
         {"620", "0.995"}, 
         {"580", "0.996"}, 
         {"546", "0.995"}, 
         {"500", "0.99"}, 
         {"460", "0.982"}, 
         {"436", "0.973"}, 
         {"420", "0.963"}, 
         {"405", "0.928"}, 
         {"400", "0.905"}, 
         {"390", "0.826"}, 
         {"380", "0.642"}, 
         {"370", "0.276"}, 
         {"365", "0.116"}});
    model.material("mat4").propertyGroup("InternalTransmittance10").func("int1").set("fununit", new String[]{"1"});
    model.material("mat4").propertyGroup("InternalTransmittance10").func("int1").set("argunit", new String[]{"nm"});
    model.material("mat4").propertyGroup("InternalTransmittance10").set("taui10", "taui10(c_const/freq)");
    model.material("mat4").propertyGroup("InternalTransmittance10").addInput("frequency");
    model.material("mat4").propertyGroup("InternalTransmittance25").func("int1").set("funcname", "taui25");
    model.material("mat4").propertyGroup("InternalTransmittance25").func("int1")
         .set("table", new String[][]{{"2500", "0.5"}, 
         {"2325", "0.63"}, 
         {"1970", "0.88"}, 
         {"1530", "0.975"}, 
         {"1060", "0.994"}, 
         {"700", "0.989"}, 
         {"660", "0.987"}, 
         {"620", "0.988"}, 
         {"580", "0.991"}, 
         {"546", "0.988"}, 
         {"500", "0.976"}, 
         {"460", "0.956"}, 
         {"436", "0.935"}, 
         {"420", "0.91"}, 
         {"405", "0.83"}, 
         {"400", "0.78"}, 
         {"390", "0.62"}, 
         {"380", "0.33"}, 
         {"370", "0.04"}});
    model.material("mat4").propertyGroup("InternalTransmittance25").func("int1").set("fununit", new String[]{"1"});
    model.material("mat4").propertyGroup("InternalTransmittance25").func("int1").set("argunit", new String[]{"nm"});
    model.material("mat4").propertyGroup("InternalTransmittance25").set("taui25", "taui25(c_const/freq)");
    model.material("mat4").propertyGroup("InternalTransmittance25").addInput("frequency");
    model.material("mat5").label("ZnS (Zinc sulfide) (Debenham 1984: Cubic ZnS; n 0.405-13 um)");
    model.material("mat5").propertyGroup("RefractiveIndex").func("an1")
         .set("expr", "sqrt((8.393)+(0.14383)/(x^2-0.058612410000000004)+(4430.99)/(x^2-1347.6241))");
    model.material("mat5").propertyGroup("RefractiveIndex").func("an1").set("fununit", "1");
    model.material("mat5").propertyGroup("RefractiveIndex").func("an1").set("argunit", new String[]{"um"});
    model.material("mat5").propertyGroup("RefractiveIndex")
         .set("n", new String[]{"an1(c_const/freq)", "0", "0", "0", "an1(c_const/freq)", "0", "0", "0", "an1(c_const/freq)"});
    model.material("mat5").propertyGroup("RefractiveIndex").addInput("frequency");
    model.material("mat6").label("MgF2 (Magnesium fluoride) (Li 1980: n(o) 0.14-7.5 um)");
    model.material("mat6").propertyGroup("RefractiveIndex").func("an1")
         .set("expr", "sqrt((1.2762)+(0.60967)*x^2/(x^2-(0.007458049600000001))+(0.0080)*x^2/(x^2-(324.0))+(2.14973)*x^2/(x^2-(625.0)))");
    model.material("mat6").propertyGroup("RefractiveIndex").func("an1").set("fununit", "1");
    model.material("mat6").propertyGroup("RefractiveIndex").func("an1").set("argunit", new String[]{"um"});
    model.material("mat6").propertyGroup("RefractiveIndex")
         .set("n", new String[]{"an1(c_const/freq)", "0", "0", "0", "an1(c_const/freq)", "0", "0", "0", "an1(c_const/freq)"});
    model.material("mat6").propertyGroup("RefractiveIndex").addInput("frequency");
    model.component("comp1").material("matlnk1").active(false);
    model.component("comp1").material("matlnk2").active(false);
    model.component("comp1").material("matlnk2").set("link", "mat5");
    model.component("comp1").material("matlnk3").active(false);
    model.component("comp1").material("matlnk3").set("link", "mat6");
    model.component("comp1").material("mat7")
         .label("MgF2 (Magnesium fluoride) (Rodr\u00edguez-de Marcos et al. 2017: n,k 0.03-2.0 um)");
    model.component("comp1").material("mat7").propertyGroup("def").set("absorption", "");
    model.component("comp1").material("mat7").propertyGroup("def").set("refractiveindex", "");
    model.component("comp1").material("mat7").propertyGroup("RefractiveIndex").func("int1").set("funcname", "nr");

    return model;
  }

  public static Model run4(Model model) {
    model.component("comp1").material("mat7").propertyGroup("RefractiveIndex").func("int1")
         .set("table", new String[][]{{"2.99919E-02", "0.925222"}, 
         {"3.01236E-02", "0.924148"}, 
         {"3.02558E-02", "0.922739"}, 
         {"3.03887E-02", "0.921308"}, 
         {"3.05221E-02", "0.920206"}, 
         {"3.06560E-02", "0.919301"}, 
         {"3.07906E-02", "0.918523"}, 
         {"3.09258E-02", "0.917841"}, 
         {"3.10616E-02", "0.917236"}, 
         {"3.11979E-02", "0.916696"}, 
         {"3.13349E-02", "0.916211"}, 
         {"3.14724E-02", "0.915775"}, 
         {"3.16106E-02", "0.915383"}, 
         {"3.17494E-02", "0.915031"}, 
         {"3.18887E-02", "0.914714"}, 
         {"3.20287E-02", "0.914430"}, 
         {"3.21693E-02", "0.914176"}, 
         {"3.23105E-02", "0.913950"}, 
         {"3.24524E-02", "0.913749"}, 
         {"3.25948E-02", "0.913572"}, 
         {"3.27379E-02", "0.913417"}, 
         {"3.28817E-02", "0.913282"}, 
         {"3.30260E-02", "0.913166"}, 
         {"3.31710E-02", "0.913068"}, 
         {"3.33166E-02", "0.912986"}, 
         {"3.34629E-02", "0.912919"}, 
         {"3.36098E-02", "0.912866"}, 
         {"3.37573E-02", "0.912826"}, 
         {"3.39055E-02", "0.912798"}, 
         {"3.40543E-02", "0.912780"}, 
         {"3.42038E-02", "0.912773"}, 
         {"3.43540E-02", "0.912774"}, 
         {"3.45048E-02", "0.912782"}, 
         {"3.46563E-02", "0.912798"}, 
         {"3.48084E-02", "0.912820"}, 
         {"3.49612E-02", "0.912848"}, 
         {"3.51147E-02", "0.912879"}, 
         {"3.52688E-02", "0.912914"}, 
         {"3.54237E-02", "0.912952"}, 
         {"3.55792E-02", "0.912992"}, 
         {"3.57354E-02", "0.913032"}, 
         {"3.58922E-02", "0.913073"}, 
         {"3.60498E-02", "0.913113"}, 
         {"3.62080E-02", "0.913151"}, 
         {"3.63670E-02", "0.913187"}, 
         {"3.65266E-02", "0.913220"}, 
         {"3.66870E-02", "0.913249"}, 
         {"3.68480E-02", "0.913273"}, 
         {"3.70098E-02", "0.913290"}, 
         {"3.71723E-02", "0.913301"}, 
         {"3.73355E-02", "0.913304"}, 
         {"3.74994E-02", "0.913298"}, 
         {"3.76640E-02", "0.913282"}, 
         {"3.78293E-02", "0.913256"}, 
         {"3.79954E-02", "0.913217"}, 
         {"3.81622E-02", "0.913165"}, 
         {"3.83297E-02", "0.913099"}, 
         {"3.84980E-02", "0.913018"}, 
         {"3.86670E-02", "0.912919"}, 
         {"3.88367E-02", "0.912803"}, 
         {"3.90072E-02", "0.912667"}, 
         {"3.91784E-02", "0.912509"}, 
         {"3.93504E-02", "0.912329"}, 
         {"3.95232E-02", "0.912124"}, 
         {"3.96967E-02", "0.911892"}, 
         {"3.98709E-02", "0.911631"}, 
         {"4.00460E-02", "0.911338"}, 
         {"4.02218E-02", "0.911008"}, 
         {"4.03983E-02", "0.910638"}, 
         {"4.05757E-02", "0.910215"}, 
         {"4.07538E-02", "0.909729"}, 
         {"4.09327E-02", "0.909201"}, 
         {"4.11124E-02", "0.908650"}, 
         {"4.12929E-02", "0.908066"}, 
         {"4.14742E-02", "0.907445"}, 
         {"4.16562E-02", "0.906781"}, 
         {"4.18391E-02", "0.906068"}, 
         {"4.20228E-02", "0.905303"}, 
         {"4.22072E-02", "0.904480"}, 
         {"4.23925E-02", "0.903592"}, 
         {"4.25786E-02", "0.902633"}, 
         {"4.27655E-02", "0.901596"}, 
         {"4.29533E-02", "0.900471"}, 
         {"4.31418E-02", "0.899248"}, 
         {"4.33312E-02", "0.897915"}, 
         {"4.35214E-02", "0.896456"}, 
         {"4.37125E-02", "0.894851"}, 
         {"4.39044E-02", "0.893073"}, 
         {"4.40971E-02", "0.891080"}, 
         {"4.42907E-02", "0.888807"}, 
         {"4.44851E-02", "0.886119"}, 
         {"4.46804E-02", "0.882553"}, 
         {"4.48766E-02", "0.878348"}, 
         {"4.50736E-02", "0.874226"}, 
         {"4.52714E-02", "0.870237"}, 
         {"4.54702E-02", "0.866251"}, 
         {"4.56698E-02", "0.862125"}, 
         {"4.58703E-02", "0.857640"}, 
         {"4.60716E-02", "0.852194"}, 
         {"4.62739E-02", "0.845459"}, 
         {"4.64770E-02", "0.838358"}, 
         {"4.66811E-02", "0.831821"}, 
         {"4.68860E-02", "0.825903"}, 
         {"4.70918E-02", "0.820443"}, 
         {"4.72985E-02", "0.815318"}, 
         {"4.75062E-02", "0.810427"}, 
         {"4.77147E-02", "0.805680"}, 
         {"4.79242E-02", "0.800983"}, 
         {"4.81346E-02", "0.796222"}, 
         {"4.83459E-02", "0.791246"}, 
         {"4.85581E-02", "0.785807"}, 
         {"4.87713E-02", "0.779386"}, 
         {"4.89854E-02", "0.770125"}, 
         {"4.92004E-02", "0.757826"}, 
         {"4.94164E-02", "0.745432"}, 
         {"4.96333E-02", "0.734677"}, 
         {"4.98512E-02", "0.725503"}, 
         {"5.00701E-02", "0.717622"}, 
         {"5.02899E-02", "0.710787"}, 
         {"5.05106E-02", "0.704736"}, 
         {"5.07324E-02", "0.698967"}, 
         {"5.09551E-02", "0.692190"}, 
         {"5.11788E-02", "0.686577"}, 
         {"5.14034E-02", "0.685944"}, 
         {"5.16291E-02", "0.686235"}, 
         {"5.18557E-02", "0.683942"}, 
         {"5.20834E-02", "0.684162"}, 
         {"5.23120E-02", "0.689812"}, 
         {"5.25417E-02", "0.697947"}, 
         {"5.27723E-02", "0.706307"}, 
         {"5.30040E-02", "0.714735"}, 
         {"5.32367E-02", "0.723456"}, 
         {"5.34704E-02", "0.732930"}, 
         {"5.37051E-02", "0.743658"}, 
         {"5.39409E-02", "0.755311"}, 
         {"5.41777E-02", "0.767008"}, 
         {"5.44155E-02", "0.778405"}, 
         {"5.46544E-02", "0.789569"}, 
         {"5.48943E-02", "0.800562"}, 
         {"5.51353E-02", "0.811426"}, 
         {"5.53773E-02", "0.822196"}, 
         {"5.56204E-02", "0.832896"}, 
         {"5.58646E-02", "0.843548"}, 
         {"5.61098E-02", "0.854171"}, 
         {"5.63562E-02", "0.864781"}, 
         {"5.66036E-02", "0.875395"}, 
         {"5.68520E-02", "0.886026"}, 
         {"5.71016E-02", "0.896693"}, 
         {"5.73523E-02", "0.907414"}, 
         {"5.76041E-02", "0.918197"}, 
         {"5.78569E-02", "0.929049"}, 
         {"5.81109E-02", "0.939987"}, 
         {"5.83660E-02", "0.951033"}, 
         {"5.86222E-02", "0.962218"}, 
         {"5.88796E-02", "0.973578"}, 
         {"5.91381E-02", "0.985170"}, 
         {"5.93977E-02", "0.997087"}, 
         {"5.96584E-02", "1.009527"}, 
         {"5.99203E-02", "1.022967"}, 
         {"6.01834E-02", "1.037218"}, 
         {"6.04476E-02", "1.050956"}, 
         {"6.07129E-02", "1.063504"}, 
         {"6.09794E-02", "1.074883"}, 
         {"6.12471E-02", "1.084463"}, 
         {"6.15160E-02", "1.093150"}, 
         {"6.17861E-02", "1.102642"}, 
         {"6.20573E-02", "1.113048"}, 
         {"6.23297E-02", "1.124223"}, 
         {"6.26033E-02", "1.136286"}, 
         {"6.28782E-02", "1.149873"}, 
         {"6.31542E-02", "1.163911"}, 
         {"6.34314E-02", "1.176999"}, 
         {"6.37099E-02", "1.189466"}, 
         {"6.39896E-02", "1.201496"}, 
         {"6.42705E-02", "1.213175"}, 
         {"6.45526E-02", "1.224552"}, 
         {"6.48360E-02", "1.235659"}, 
         {"6.51206E-02", "1.246522"}, 
         {"6.54065E-02", "1.257165"}, 
         {"6.56936E-02", "1.267616"}, 
         {"6.59820E-02", "1.277927"}, 
         {"6.62717E-02", "1.288129"}, 
         {"6.65626E-02", "1.298106"}, 
         {"6.68548E-02", "1.307786"}, 
         {"6.71483E-02", "1.317297"}, 
         {"6.74431E-02", "1.326540"}, 
         {"6.77391E-02", "1.335435"}, 
         {"6.80365E-02", "1.343809"}, 
         {"6.83352E-02", "1.351110"}, 
         {"6.86352E-02", "1.357095"}, 
         {"6.89365E-02", "1.361815"}, 
         {"6.92391E-02", "1.365223"}, 
         {"6.95430E-02", "1.367051"}, 
         {"6.98483E-02", "1.366656"}, 
         {"7.01550E-02", "1.365613"}, 
         {"7.04629E-02", "1.366384"}, 
         {"7.07723E-02", "1.368730"}, 
         {"7.10829E-02", "1.371698"}, 
         {"7.13950E-02", "1.374723"}, 
         {"7.17084E-02", "1.376843"}, 
         {"7.20232E-02", "1.380593"}, 
         {"7.23394E-02", "1.390648"}, 
         {"7.26569E-02", "1.404711"}, 
         {"7.29759E-02", "1.417600"}, 
         {"7.32962E-02", "1.428864"}, 
         {"7.36180E-02", "1.439793"}, 
         {"7.39412E-02", "1.450948"}, 
         {"7.42658E-02", "1.462661"}, 
         {"7.45918E-02", "1.474084"}, 
         {"7.49193E-02", "1.483691"}, 
         {"7.52481E-02", "1.491426"}, 
         {"7.55785E-02", "1.497995"}, 
         {"7.59103E-02", "1.503640"}, 
         {"7.62435E-02", "1.508482"}, 
         {"7.65782E-02", "1.512582"}, 
         {"7.69144E-02", "1.515960"}, 
         {"7.72520E-02", "1.518591"}, 
         {"7.75911E-02", "1.520463"}, 
         {"7.79318E-02", "1.521958"}, 
         {"7.82739E-02", "1.523838"}, 
         {"7.86175E-02", "1.526195"}, 
         {"7.89626E-02", "1.528649"}, 
         {"7.93092E-02", "1.531088"}, 
         {"7.96574E-02", "1.533458"}, 
         {"8.00071E-02", "1.535733"}, 
         {"8.03583E-02", "1.537895"}, 
         {"8.07111E-02", "1.539937"}, 
         {"8.10654E-02", "1.541852"}, 
         {"8.14213E-02", "1.543639"}, 
         {"8.17787E-02", "1.545303"}, 
         {"8.21377E-02", "1.546854"}, 
         {"8.24983E-02", "1.548272"}, 
         {"8.28604E-02", "1.549378"}, 
         {"8.32242E-02", "1.549932"}, 
         {"8.35895E-02", "1.550008"}, 
         {"8.39565E-02", "1.550052"}, 
         {"8.43250E-02", "1.550201"}, 
         {"8.46952E-02", "1.550279"}, 
         {"8.50670E-02", "1.550241"}, 
         {"8.54405E-02", "1.550070"}, 
         {"8.58155E-02", "1.549761"}, 
         {"8.61923E-02", "1.549318"}, 
         {"8.65706E-02", "1.548716"}, 
         {"8.69507E-02", "1.547536"}, 
         {"8.73324E-02", "1.545100"}, 
         {"8.77158E-02", "1.541323"}, 
         {"8.81008E-02", "1.536346"}, 
         {"8.84876E-02", "1.530100"}, 
         {"8.88760E-02", "1.526843"}, 
         {"8.92662E-02", "1.533004"}, 
         {"8.96581E-02", "1.542282"}, 
         {"9.00517E-02", "1.544354"}, 
         {"9.04470E-02", "1.539810"}, 
         {"9.08440E-02", "1.537663"}, 
         {"9.12428E-02", "1.542344"}, 
         {"9.16434E-02", "1.550929"}, 
         {"9.20457E-02", "1.561941"}, 
         {"9.24497E-02", "1.570120"}, 
         {"9.28556E-02", "1.572935"}, 
         {"9.32632E-02", "1.572951"}, 
         {"9.36726E-02", "1.569040"}, 
         {"9.40838E-02", "1.560470"}, 
         {"9.44969E-02", "1.548921"}, 
         {"9.49117E-02", "1.536161"}, 
         {"9.53284E-02", "1.527699"}, 
         {"9.57468E-02", "1.524122"}, 
         {"9.61672E-02", "1.520482"}, 
         {"9.65893E-02", "1.516092"}, 
         {"9.70133E-02", "1.509704"}, 
         {"9.74392E-02", "1.493412"}, 
         {"9.78670E-02", "1.468672"}, 
         {"9.82966E-02", "1.451511"}, 
         {"9.87281E-02", "1.443294"}, 
         {"9.91615E-02", "1.430197"}, 
         {"9.95968E-02", "1.410291"}, 
         {"1.00034E-01", "1.394670"}, 
         {"1.00473E-01", "1.394182"}, 
         {"1.00914E-01", "1.406965"}, 
         {"1.01357E-01", "1.426614"}, 
         {"1.01802E-01", "1.450337"}, 
         {"1.02249E-01", "1.478988"}, 
         {"1.02698E-01", "1.518661"}, 
         {"1.03149E-01", "1.569325"}, 
         {"1.03602E-01", "1.637783"}, 
         {"1.04056E-01", "1.741224"}, 
         {"1.04513E-01", "1.880297"}, 
         {"1.04972E-01", "2.014147"}, 
         {"1.05433E-01", "2.097264"}, 
         {"1.05896E-01", "2.121767"}, 
         {"1.06361E-01", "2.111039"}, 
         {"1.06827E-01", "2.101072"}, 
         {"1.07296E-01", "2.106338"}, 
         {"1.07767E-01", "2.123857"}, 
         {"1.08241E-01", "2.142700"}, 
         {"1.08716E-01", "2.141217"}, 
         {"1.09193E-01", "2.106695"}, 
         {"1.09672E-01", "2.052598"}, 
         {"1.10154E-01", "2.006438"}, 
         {"1.10637E-01", "1.974922"}, 
         {"1.11123E-01", "1.950236"}, 
         {"1.11611E-01", "1.929626"}, 
         {"1.12101E-01", "1.911703"}, 
         {"1.12593E-01", "1.895621"}, 
         {"1.13087E-01", "1.880776"}, 
         {"1.13584E-01", "1.866709"}, 
         {"1.14082E-01", "1.853466"}, 
         {"1.14583E-01", "1.841058"}, 
         {"1.15086E-01", "1.829123"}, 
         {"1.15591E-01", "1.817382"}, 
         {"1.16099E-01", "1.805479"}, 
         {"1.16608E-01", "1.792667"}, 
         {"1.17120E-01", "1.780255"}, 
         {"1.17634E-01", "1.769535"}, 
         {"1.18151E-01", "1.759754"}, 
         {"1.18669E-01", "1.750613"}, 
         {"1.19190E-01", "1.741882"}, 
         {"1.19714E-01", "1.733613"}, 
         {"1.20239E-01", "1.725884"}, 
         {"1.20767E-01", "1.718549"}, 
         {"1.21297E-01", "1.711487"}, 
         {"1.21830E-01", "1.704700"}, 
         {"1.22364E-01", "1.698347"}, 
         {"1.22902E-01", "1.692497"}, 
         {"1.23441E-01", "1.687244"}, 
         {"1.23983E-01", "1.682190"}, 
         {"1.24527E-01", "1.676660"}, 
         {"1.25074E-01", "1.670330"}, 
         {"1.25623E-01", "1.663965"}, 
         {"1.26175E-01", "1.658303"}, 
         {"1.26728E-01", "1.653062"}, 
         {"1.27285E-01", "1.648120"}, 
         {"1.27844E-01", "1.643419"}, 
         {"1.28405E-01", "1.638921"}, 
         {"1.28968E-01", "1.634597"}, 
         {"1.29535E-01", "1.630418"}, 
         {"1.30103E-01", "1.626390"}, 
         {"1.30674E-01", "1.622525"}, 
         {"1.31248E-01", "1.618808"}, 
         {"1.31824E-01", "1.615223"}, 
         {"1.32403E-01", "1.611762"}, 
         {"1.32984E-01", "1.608424"}, 
         {"1.33568E-01", "1.605201"}, 
         {"1.34154E-01", "1.602063"}, 
         {"1.34743E-01", "1.598988"}, 
         {"1.35335E-01", "1.595979"}, 
         {"1.35929E-01", "1.593031"}, 
         {"1.36526E-01", "1.590134"}, 
         {"1.37125E-01", "1.587268"}, 
         {"1.37727E-01", "1.584389"}, 
         {"1.38331E-01", "1.581601"}, 
         {"1.38939E-01", "1.579009"}, 
         {"1.39549E-01", "1.576586"}, 
         {"1.40161E-01", "1.574295"}, 
         {"1.40777E-01", "1.572076"}, 
         {"1.41395E-01", "1.569897"}, 
         {"1.42015E-01", "1.567720"}, 
         {"1.42639E-01", "1.565534"}, 
         {"1.43265E-01", "1.563341"}, 
         {"1.43894E-01", "1.561128"}, 
         {"1.44525E-01", "1.558963"}, 
         {"1.45160E-01", "1.556899"}, 
         {"1.45797E-01", "1.554910"}, 
         {"1.46437E-01", "1.552990"}, 
         {"1.47080E-01", "1.551137"}, 
         {"1.47726E-01", "1.549326"}, 
         {"1.48374E-01", "1.547537"}, 
         {"1.49026E-01", "1.545778"}, 
         {"1.49680E-01", "1.544049"}, 
         {"1.50337E-01", "1.542352"}, 
         {"1.50997E-01", "1.540682"}, 
         {"1.51660E-01", "1.539036"}, 
         {"1.52325E-01", "1.537423"}, 
         {"1.52994E-01", "1.535856"}, 
         {"1.53666E-01", "1.534329"}, 
         {"1.54340E-01", "1.532841"}, 
         {"1.55018E-01", "1.531395"}, 
         {"1.55698E-01", "1.529999"}, 
         {"1.56382E-01", "1.528655"}, 
         {"1.57068E-01", "1.527324"}, 
         {"1.57758E-01", "1.525980"}, 
         {"1.58450E-01", "1.524637"}, 
         {"1.59146E-01", "1.523300"}, 
         {"1.59845E-01", "1.521966"}, 
         {"1.60546E-01", "1.520641"}, 
         {"1.61251E-01", "1.519329"}, 
         {"1.61959E-01", "1.518027"}, 
         {"1.62670E-01", "1.516726"}, 
         {"1.63384E-01", "1.515418"}, 
         {"1.64101E-01", "1.514134"}, 
         {"1.64822E-01", "1.512894"}, 
         {"1.65545E-01", "1.511685"}, 
         {"1.66272E-01", "1.510503"}, 
         {"1.67002E-01", "1.509344"}, 
         {"1.67735E-01", "1.508206"}, 
         {"1.68471E-01", "1.507089"}, 
         {"1.69211E-01", "1.505992"}, 
         {"1.69954E-01", "1.504913"}, 
         {"1.70700E-01", "1.503851"}, 
         {"1.71449E-01", "1.502807"}, 
         {"1.72202E-01", "1.501780"}, 
         {"1.72958E-01", "1.500768"}, 
         {"1.73717E-01", "1.499772"}, 
         {"1.74480E-01", "1.498792"}, 
         {"1.75246E-01", "1.497826"}, 
         {"1.76015E-01", "1.496874"}, 
         {"1.76788E-01", "1.495935"}, 
         {"1.77564E-01", "1.495009"}, 
         {"1.78343E-01", "1.494096"}, 
         {"1.79126E-01", "1.493195"}, 
         {"1.79912E-01", "1.492306"}, 
         {"1.80702E-01", "1.491427"}, 
         {"1.81496E-01", "1.490558"}, 
         {"1.82292E-01", "1.489694"}, 
         {"1.83093E-01", "1.488843"}, 
         {"1.83896E-01", "1.488009"}, 
         {"1.84704E-01", "1.487190"}, 
         {"1.85514E-01", "1.486385"}, 
         {"1.86329E-01", "1.485592"}, 
         {"1.87147E-01", "1.484811"}, 
         {"1.87968E-01", "1.484042"}, 
         {"1.88793E-01", "1.483285"}, 
         {"1.89622E-01", "1.482538"}, 
         {"1.90455E-01", "1.481802"}, 
         {"1.91291E-01", "1.481077"}, 
         {"1.92131E-01", "1.480362"}, 
         {"1.92974E-01", "1.479657"}, 
         {"1.93821E-01", "1.478961"}, 
         {"1.94672E-01", "1.478275"}, 
         {"1.95527E-01", "1.477599"}, 
         {"1.96385E-01", "1.476931"}, 
         {"1.97247E-01", "1.476273"}, 
         {"1.98113E-01", "1.475624"}, 
         {"1.98983E-01", "1.474983"}, 
         {"1.99856E-01", "1.474350"}, 
         {"2.00733E-01", "1.473726"}, 
         {"2.01615E-01", "1.473110"}, 
         {"2.02500E-01", "1.472503"}, 
         {"2.03389E-01", "1.471903"}, 
         {"2.04282E-01", "1.471311"}, 
         {"2.05178E-01", "1.470727"}, 
         {"2.06079E-01", "1.470150"}, 
         {"2.06984E-01", "1.469580"}, 
         {"2.07892E-01", "1.469017"}, 
         {"2.08805E-01", "1.468462"}, 
         {"2.09722E-01", "1.467914"}, 
         {"2.10642E-01", "1.467373"}, 
         {"2.11567E-01", "1.466838"}, 
         {"2.12496E-01", "1.466310"}, 
         {"2.13429E-01", "1.465789"}, 
         {"2.14365E-01", "1.465274"}, 
         {"2.15307E-01", "1.464765"}, 
         {"2.16252E-01", "1.464263"}, 
         {"2.17201E-01", "1.463766"}, 
         {"2.18155E-01", "1.463276"}, 
         {"2.19112E-01", "1.462792"}, 
         {"2.20074E-01", "1.462313"}, 
         {"2.21040E-01", "1.461840"}, 
         {"2.22011E-01", "1.461373"}, 
         {"2.22985E-01", "1.460912"}, 
         {"2.23964E-01", "1.460456"}, 
         {"2.24947E-01", "1.460006"}, 
         {"2.25935E-01", "1.459560"}, 
         {"2.26927E-01", "1.459120"}, 
         {"2.27923E-01", "1.458686"}, 
         {"2.28923E-01", "1.458256"}, 
         {"2.29928E-01", "1.457831"}, 
         {"2.30938E-01", "1.457412"}, 
         {"2.31951E-01", "1.456997"}, 
         {"2.32970E-01", "1.456587"}, 
         {"2.33992E-01", "1.456181"}, 
         {"2.35020E-01", "1.455781"}, 
         {"2.36051E-01", "1.455385"}, 
         {"2.37088E-01", "1.454993"}, 
         {"2.38128E-01", "1.454606"}, 
         {"2.39174E-01", "1.454224"}, 
         {"2.40224E-01", "1.453846"}, 
         {"2.41278E-01", "1.453472"}, 
         {"2.42337E-01", "1.453102"}, 
         {"2.43401E-01", "1.452737"}, 
         {"2.44470E-01", "1.452375"}, 
         {"2.45543E-01", "1.452018"}, 
         {"2.46621E-01", "1.451664"}, 
         {"2.47703E-01", "1.451315"}, 
         {"2.48791E-01", "1.450969"}, 
         {"2.49883E-01", "1.450628"}, 
         {"2.50980E-01", "1.450290"}, 
         {"2.52082E-01", "1.449955"}, 
         {"2.53188E-01", "1.449625"}, 
         {"2.54300E-01", "1.449299"}, 
         {"2.55416E-01", "1.448975"}, 
         {"2.56537E-01", "1.448655"}, 
         {"2.57664E-01", "1.448339"}, 
         {"2.58795E-01", "1.448027"}, 
         {"2.59931E-01", "1.447717"}, 
         {"2.61072E-01", "1.447411"}, 
         {"2.62218E-01", "1.447109"}, 
         {"2.63369E-01", "1.446809"}, 
         {"2.64525E-01", "1.446513"}, 
         {"2.65687E-01", "1.446220"}, 
         {"2.66853E-01", "1.445930"}, 
         {"2.68024E-01", "1.445644"}, 
         {"2.69201E-01", "1.445360"}, 
         {"2.70383E-01", "1.445079"}, 
         {"2.71570E-01", "1.444802"}, 
         {"2.72762E-01", "1.444527"}, 
         {"2.73959E-01", "1.444255"}, 
         {"2.75162E-01", "1.443987"}, 
         {"2.76370E-01", "1.443721"}, 
         {"2.77583E-01", "1.443457"}, 
         {"2.78802E-01", "1.443197"}, 
         {"2.80025E-01", "1.442939"}, 
         {"2.81255E-01", "1.442684"}, 
         {"2.82489E-01", "1.442432"}, 
         {"2.83730E-01", "1.442183"}, 
         {"2.84975E-01", "1.441936"}, 
         {"2.86226E-01", "1.441691"}, 
         {"2.87483E-01", "1.441449"}, 
         {"2.88745E-01", "1.441210"}, 
         {"2.90012E-01", "1.440973"}, 
         {"2.91285E-01", "1.440738"}, 
         {"2.92564E-01", "1.440506"}, 
         {"2.93848E-01", "1.440277"}, 
         {"2.95138E-01", "1.440050"}, 
         {"2.96434E-01", "1.439825"}, 
         {"2.97735E-01", "1.439602"}, 
         {"2.99042E-01", "1.439382"}, 
         {"3.00355E-01", "1.439164"}, 
         {"3.01674E-01", "1.438948"}, 
         {"3.02998E-01", "1.438735"}, 
         {"3.04328E-01", "1.438524"}, 
         {"3.05664E-01", "1.438314"}, 
         {"3.07006E-01", "1.438107"}, 
         {"3.08354E-01", "1.437902"}, 
         {"3.09707E-01", "1.437699"}, 
         {"3.11067E-01", "1.437498"}, 
         {"3.12432E-01", "1.437299"}, 
         {"3.13804E-01", "1.437102"}, 
         {"3.15182E-01", "1.436907"}, 
         {"3.16565E-01", "1.436714"}, 
         {"3.17955E-01", "1.436523"}, 
         {"3.19351E-01", "1.436334"}, 
         {"3.20753E-01", "1.436147"}, 
         {"3.22161E-01", "1.435962"}, 
         {"3.23575E-01", "1.435779"}, 
         {"3.24995E-01", "1.435597"}, 
         {"3.26422E-01", "1.435417"}, 
         {"3.27855E-01", "1.435239"}, 
         {"3.29294E-01", "1.435063"}, 
         {"3.30740E-01", "1.434889"}, 
         {"3.32192E-01", "1.434716"}, 
         {"3.33650E-01", "1.434545"}, 
         {"3.35115E-01", "1.434375"}, 
         {"3.36586E-01", "1.434208"}, 
         {"3.38063E-01", "1.434041"}, 
         {"3.39547E-01", "1.433877"}, 
         {"3.41038E-01", "1.433715"}, 
         {"3.42535E-01", "1.433554"}, 
         {"3.44039E-01", "1.433395"}, 
         {"3.45549E-01", "1.433237"}, 
         {"3.47066E-01", "1.433080"}, 
         {"3.48590E-01", "1.432926"}, 
         {"3.50120E-01", "1.432772"}, 
         {"3.51657E-01", "1.432621"}, 
         {"3.53201E-01", "1.432470"}, 
         {"3.54751E-01", "1.432321"}, 
         {"3.56309E-01", "1.432174"}, 
         {"3.57873E-01", "1.432028"}, 
         {"3.59444E-01", "1.431883"}, 
         {"3.61022E-01", "1.431740"}, 
         {"3.62607E-01", "1.431598"}, 
         {"3.64198E-01", "1.431458"}, 
         {"3.65797E-01", "1.431318"}, 
         {"3.67403E-01", "1.431181"}, 
         {"3.69016E-01", "1.431045"}, 
         {"3.70636E-01", "1.430910"}, 
         {"3.72263E-01", "1.430776"}, 
         {"3.73897E-01", "1.430644"}, 
         {"3.75538E-01", "1.430513"}, 
         {"3.77187E-01", "1.430383"}, 
         {"3.78843E-01", "1.430254"}, 
         {"3.80506E-01", "1.430127"}, 
         {"3.82176E-01", "1.430001"}, 
         {"3.83854E-01", "1.429876"}, 
         {"3.85539E-01", "1.429752"}, 
         {"3.87232E-01", "1.429629"}, 
         {"3.88931E-01", "1.429508"}, 
         {"3.90639E-01", "1.429388"}, 
         {"3.92354E-01", "1.429269"}, 
         {"3.94076E-01", "1.429151"}, 
         {"3.95806E-01", "1.429034"}, 
         {"3.97544E-01", "1.428919"}, 
         {"3.99289E-01", "1.428804"}, 
         {"4.01042E-01", "1.428690"}, 
         {"4.02802E-01", "1.428578"}, 
         {"4.04570E-01", "1.428466"}, 
         {"4.06346E-01", "1.428356"}, 
         {"4.08130E-01", "1.428247"}, 
         {"4.09922E-01", "1.428139"}, 
         {"4.11721E-01", "1.428032"}, 
         {"4.13529E-01", "1.427926"}, 
         {"4.15344E-01", "1.427820"}, 
         {"4.17167E-01", "1.427716"}, 
         {"4.18999E-01", "1.427613"}, 
         {"4.20838E-01", "1.427510"}, 
         {"4.22686E-01", "1.427410"}, 
         {"4.24541E-01", "1.427309"}, 
         {"4.26405E-01", "1.427210"}, 
         {"4.28277E-01", "1.427112"}, 
         {"4.30157E-01", "1.427014"}, 
         {"4.32045E-01", "1.426918"}, 
         {"4.33942E-01", "1.426822"}, 
         {"4.35847E-01", "1.426727"}, 
         {"4.37760E-01", "1.426633"}, 
         {"4.39682E-01", "1.426540"}, 
         {"4.41612E-01", "1.426448"}, 
         {"4.43551E-01", "1.426357"}, 
         {"4.45498E-01", "1.426267"}, 
         {"4.47453E-01", "1.426177"}, 
         {"4.49418E-01", "1.426088"}, 
         {"4.51391E-01", "1.426000"}, 
         {"4.53372E-01", "1.425913"}, 
         {"4.55362E-01", "1.425827"}, 
         {"4.57361E-01", "1.425742"}, 
         {"4.59369E-01", "1.425657"}, 
         {"4.61386E-01", "1.425573"}, 
         {"4.63411E-01", "1.425490"}, 
         {"4.65446E-01", "1.425408"}, 
         {"4.67489E-01", "1.425326"}, 
         {"4.69541E-01", "1.425246"}, 
         {"4.71602E-01", "1.425166"}, 
         {"4.73673E-01", "1.425086"}, 
         {"4.75752E-01", "1.425008"}, 
         {"4.77841E-01", "1.424930"}, 
         {"4.79938E-01", "1.424853"}, 
         {"4.82045E-01", "1.424776"}, 
         {"4.84161E-01", "1.424700"}, 
         {"4.86287E-01", "1.424626"}, 
         {"4.88421E-01", "1.424552"}, 
         {"4.90566E-01", "1.424478"}, 
         {"4.92719E-01", "1.424405"}, 
         {"4.94882E-01", "1.424333"}, 
         {"4.97055E-01", "1.424261"}, 
         {"4.99237E-01", "1.424190"}, 
         {"5.01428E-01", "1.424120"}, 
         {"5.03629E-01", "1.424051"}, 
         {"5.05840E-01", "1.423982"}, 
         {"5.08061E-01", "1.423913"}, 
         {"5.10291E-01", "1.423846"}, 
         {"5.12531E-01", "1.423778"}, 
         {"5.14781E-01", "1.423712"}, 
         {"5.17041E-01", "1.423646"}, 
         {"5.19311E-01", "1.423581"}, 
         {"5.21591E-01", "1.423516"}, 
         {"5.23880E-01", "1.423452"}, 
         {"5.26180E-01", "1.423389"}, 
         {"5.28490E-01", "1.423326"}, 
         {"5.30810E-01", "1.423263"}, 
         {"5.33140E-01", "1.423201"}, 
         {"5.35481E-01", "1.423141"}, 
         {"5.37831E-01", "1.423080"}, 
         {"5.40192E-01", "1.423020"}, 
         {"5.42564E-01", "1.422961"}, 
         {"5.44946E-01", "1.422902"}, 
         {"5.47338E-01", "1.422843"}, 
         {"5.49741E-01", "1.422785"}, 
         {"5.52154E-01", "1.422727"}, 
         {"5.54578E-01", "1.422671"}, 
         {"5.57012E-01", "1.422615"}, 
         {"5.59458E-01", "1.422559"}, 
         {"5.61914E-01", "1.422504"}, 
         {"5.64380E-01", "1.422449"}, 
         {"5.66858E-01", "1.422394"}, 
         {"5.69346E-01", "1.422340"}, 
         {"5.71846E-01", "1.422287"}, 
         {"5.74356E-01", "1.422235"}, 
         {"5.76877E-01", "1.422182"}, 
         {"5.79410E-01", "1.422130"}, 
         {"5.81953E-01", "1.422079"}, 
         {"5.84508E-01", "1.422028"}, 
         {"5.87074E-01", "1.421977"}, 
         {"5.89651E-01", "1.421927"}, 
         {"5.92240E-01", "1.421878"}, 
         {"5.94840E-01", "1.421829"}, 
         {"5.97451E-01", "1.421780"}, 
         {"6.00074E-01", "1.421732"}, 
         {"6.02708E-01", "1.421684"}, 
         {"6.05354E-01", "1.421636"}, 
         {"6.08011E-01", "1.421590"}, 
         {"6.10680E-01", "1.421543"}, 
         {"6.13361E-01", "1.421497"}, 
         {"6.16054E-01", "1.421452"}, 
         {"6.18758E-01", "1.421406"}, 
         {"6.21475E-01", "1.421361"}, 
         {"6.24203E-01", "1.421317"}, 
         {"6.26943E-01", "1.421273"}, 
         {"6.29695E-01", "1.421229"}, 
         {"6.32459E-01", "1.421186"}, 
         {"6.35236E-01", "1.421143"}, 
         {"6.38025E-01", "1.421101"}, 
         {"6.40825E-01", "1.421059"}, 
         {"6.43639E-01", "1.421017"}, 
         {"6.46464E-01", "1.420975"}, 
         {"6.49302E-01", "1.420935"}, 
         {"6.52152E-01", "1.420894"}, 
         {"6.55015E-01", "1.420854"}, 
         {"6.57891E-01", "1.420814"}, 
         {"6.60779E-01", "1.420775"}, 
         {"6.63680E-01", "1.420736"}, 
         {"6.66593E-01", "1.420697"}, 
         {"6.69519E-01", "1.420658"}, 
         {"6.72458E-01", "1.420620"}, 
         {"6.75410E-01", "1.420583"}, 
         {"6.78375E-01", "1.420545"}, 
         {"6.81353E-01", "1.420508"}, 
         {"6.84345E-01", "1.420471"}, 
         {"6.87349E-01", "1.420435"}, 
         {"6.90366E-01", "1.420398"}, 
         {"6.93397E-01", "1.420363"}, 
         {"6.96441E-01", "1.420328"}, 
         {"6.99498E-01", "1.420293"}, 
         {"7.02569E-01", "1.420258"}, 
         {"7.05653E-01", "1.420223"}, 
         {"7.08751E-01", "1.420189"}, 
         {"7.11862E-01", "1.420155"}, 
         {"7.14987E-01", "1.420121"}, 
         {"7.18126E-01", "1.420088"}, 
         {"7.21278E-01", "1.420055"}, 
         {"7.24445E-01", "1.420023"}, 
         {"7.27625E-01", "1.419990"}, 
         {"7.30819E-01", "1.419958"}, 
         {"7.34027E-01", "1.419926"}, 
         {"7.37250E-01", "1.419895"}, 
         {"7.40486E-01", "1.419863"}, 
         {"7.43737E-01", "1.419832"}, 
         {"7.47002E-01", "1.419802"}, 
         {"7.50281E-01", "1.419772"}, 
         {"7.53575E-01", "1.419742"}, 
         {"7.56883E-01", "1.419712"}, 
         {"7.60205E-01", "1.419682"}, 
         {"7.63543E-01", "1.419653"}, 
         {"7.66895E-01", "1.419623"}, 
         {"7.70261E-01", "1.419594"}, 
         {"7.73642E-01", "1.419566"}, 
         {"7.77039E-01", "1.419538"}, 
         {"7.80450E-01", "1.419510"}, 
         {"7.83876E-01", "1.419482"}, 
         {"7.87317E-01", "1.419454"}, 
         {"7.90773E-01", "1.419427"}, 
         {"7.94245E-01", "1.419400"}, 
         {"7.97731E-01", "1.419373"}, 
         {"8.01233E-01", "1.419347"}, 
         {"8.04751E-01", "1.419320"}, 
         {"8.08283E-01", "1.419294"}, 
         {"8.11832E-01", "1.419268"}, 
         {"8.15396E-01", "1.419243"}, 
         {"8.18975E-01", "1.419217"}, 
         {"8.22570E-01", "1.419192"}, 
         {"8.26181E-01", "1.419167"}, 
         {"8.29808E-01", "1.419142"}, 
         {"8.33451E-01", "1.419118"}, 
         {"8.37110E-01", "1.419094"}, 
         {"8.40785E-01", "1.419070"}, 
         {"8.44476E-01", "1.419046"}, 
         {"8.48183E-01", "1.419022"}, 
         {"8.51906E-01", "1.418998"}, 
         {"8.55646E-01", "1.418975"}, 
         {"8.59402E-01", "1.418952"}, 
         {"8.63175E-01", "1.418929"}, 
         {"8.66964E-01", "1.418907"}, 
         {"8.70770E-01", "1.418884"}, 
         {"8.74593E-01", "1.418862"}, 
         {"8.78432E-01", "1.418840"}, 
         {"8.82288E-01", "1.418818"}, 
         {"8.86161E-01", "1.418796"}, 
         {"8.90052E-01", "1.418775"}, 
         {"8.93959E-01", "1.418753"}, 
         {"8.97883E-01", "1.418732"}, 
         {"9.01825E-01", "1.418712"}, 
         {"9.05784E-01", "1.418691"}, 
         {"9.09760E-01", "1.418671"}, 
         {"9.13754E-01", "1.418650"}, 
         {"9.17765E-01", "1.418630"}, 
         {"9.21794E-01", "1.418610"}, 
         {"9.25841E-01", "1.418590"}, 
         {"9.29905E-01", "1.418570"}, 
         {"9.33987E-01", "1.418551"}, 
         {"9.38087E-01", "1.418532"}, 
         {"9.42205E-01", "1.418513"}, 
         {"9.46342E-01", "1.418494"}, 
         {"9.50496E-01", "1.418475"}, 
         {"9.54669E-01", "1.418457"}, 
         {"9.58859E-01", "1.418438"}, 
         {"9.63069E-01", "1.418420"}, 
         {"9.67296E-01", "1.418401"}, 
         {"9.71543E-01", "1.418383"}, 
         {"9.75808E-01", "1.418366"}, 
         {"9.80092E-01", "1.418348"}, 
         {"9.84394E-01", "1.418331"}, 
         {"9.88715E-01", "1.418314"}, 
         {"9.93056E-01", "1.418296"}, 
         {"9.97415E-01", "1.418279"}, 
         {"1.00179E+00", "1.418262"}, 
         {"1.00619E+00", "1.418246"}, 
         {"1.01061E+00", "1.418229"}, 
         {"1.01505E+00", "1.418212"}, 
         {"1.01950E+00", "1.418196"}, 
         {"1.02398E+00", "1.418180"}, 
         {"1.02847E+00", "1.418164"}, 
         {"1.03299E+00", "1.418149"}, 
         {"1.03752E+00", "1.418133"}, 
         {"1.04208E+00", "1.418117"}, 
         {"1.04665E+00", "1.418102"}, 
         {"1.05125E+00", "1.418086"}, 
         {"1.05586E+00", "1.418071"}, 
         {"1.06050E+00", "1.418056"}, 
         {"1.06515E+00", "1.418041"}, 
         {"1.06983E+00", "1.418027"}, 
         {"1.07452E+00", "1.418012"}, 
         {"1.07924E+00", "1.417998"}, 
         {"1.08398E+00", "1.417983"}, 
         {"1.08874E+00", "1.417969"}, 
         {"1.09352E+00", "1.417955"}, 
         {"1.09832E+00", "1.417941"}, 
         {"1.10314E+00", "1.417927"}, 
         {"1.10798E+00", "1.417913"}, 
         {"1.11284E+00", "1.417899"}, 
         {"1.11773E+00", "1.417886"}, 
         {"1.12264E+00", "1.417872"}, 
         {"1.12756E+00", "1.417859"}, 
         {"1.13251E+00", "1.417846"}, 
         {"1.13749E+00", "1.417833"}, 
         {"1.14248E+00", "1.417820"}, 
         {"1.14750E+00", "1.417807"}, 
         {"1.15253E+00", "1.417795"}, 
         {"1.15759E+00", "1.417782"}, 
         {"1.16267E+00", "1.417769"}, 
         {"1.16778E+00", "1.417757"}, 
         {"1.17290E+00", "1.417745"}, 
         {"1.17805E+00", "1.417733"}, 
         {"1.18322E+00", "1.417721"}, 
         {"1.18842E+00", "1.417709"}, 
         {"1.19364E+00", "1.417697"}, 
         {"1.19888E+00", "1.417685"}, 
         {"1.20414E+00", "1.417674"}, 
         {"1.20942E+00", "1.417662"}, 
         {"1.21473E+00", "1.417650"}, 
         {"1.22007E+00", "1.417639"}, 
         {"1.22542E+00", "1.417628"}, 
         {"1.23080E+00", "1.417617"}, 
         {"1.23621E+00", "1.417606"}, 
         {"1.24163E+00", "1.417595"}, 
         {"1.24708E+00", "1.417584"}, 
         {"1.25256E+00", "1.417574"}, 
         {"1.25806E+00", "1.417563"}, 
         {"1.26358E+00", "1.417552"}, 
         {"1.26913E+00", "1.417542"}, 
         {"1.27470E+00", "1.417531"}, 
         {"1.28029E+00", "1.417521"}, 
         {"1.28591E+00", "1.417511"}, 
         {"1.29156E+00", "1.417500"}, 
         {"1.29723E+00", "1.417490"}, 
         {"1.30292E+00", "1.417481"}, 
         {"1.30864E+00", "1.417471"}, 
         {"1.31439E+00", "1.417461"}, 
         {"1.32016E+00", "1.417452"}, 
         {"1.32595E+00", "1.417442"}, 
         {"1.33177E+00", "1.417433"}, 
         {"1.33762E+00", "1.417423"}, 
         {"1.34349E+00", "1.417414"}, 
         {"1.34939E+00", "1.417405"}, 
         {"1.35531E+00", "1.417395"}, 
         {"1.36126E+00", "1.417386"}, 
         {"1.36724E+00", "1.417377"}, 
         {"1.37324E+00", "1.417368"}, 
         {"1.37927E+00", "1.417360"}, 
         {"1.38532E+00", "1.417351"}, 
         {"1.39141E+00", "1.417342"}, 
         {"1.39751E+00", "1.417334"}, 
         {"1.40365E+00", "1.417325"}, 
         {"1.40981E+00", "1.417317"}, 
         {"1.41600E+00", "1.417308"}, 
         {"1.42222E+00", "1.417300"}, 
         {"1.42846E+00", "1.417292"}, 
         {"1.43473E+00", "1.417283"}, 
         {"1.44103E+00", "1.417275"}, 
         {"1.44735E+00", "1.417267"}, 
         {"1.45371E+00", "1.417259"}, 
         {"1.46009E+00", "1.417251"}, 
         {"1.46650E+00", "1.417244"}, 
         {"1.47294E+00", "1.417236"}, 
         {"1.47940E+00", "1.417228"}, 
         {"1.48590E+00", "1.417221"}, 
         {"1.49242E+00", "1.417213"}, 
         {"1.49897E+00", "1.417206"}, 
         {"1.50555E+00", "1.417198"}, 
         {"1.51216E+00", "1.417191"}, 
         {"1.51880E+00", "1.417184"}, 
         {"1.52547E+00", "1.417176"}, 
         {"1.53216E+00", "1.417169"}, 
         {"1.53889E+00", "1.417162"}, 
         {"1.54565E+00", "1.417155"}, 
         {"1.55243E+00", "1.417148"}, 
         {"1.55925E+00", "1.417141"}, 
         {"1.56609E+00", "1.417134"}, 
         {"1.57297E+00", "1.417128"}, 
         {"1.57987E+00", "1.417121"}, 
         {"1.58681E+00", "1.417114"}, 
         {"1.59377E+00", "1.417108"}, 
         {"1.60077E+00", "1.417101"}, 
         {"1.60780E+00", "1.417095"}, 
         {"1.61485E+00", "1.417088"}, 
         {"1.62194E+00", "1.417081"}, 
         {"1.62906E+00", "1.417075"}, 
         {"1.63621E+00", "1.417069"}, 
         {"1.64340E+00", "1.417063"}, 
         {"1.65061E+00", "1.417057"}, 
         {"1.65786E+00", "1.417051"}, 
         {"1.66514E+00", "1.417045"}, 
         {"1.67245E+00", "1.417039"}, 
         {"1.67979E+00", "1.417033"}, 
         {"1.68716E+00", "1.417027"}, 
         {"1.69457E+00", "1.417021"}, 
         {"1.70201E+00", "1.417015"}, 
         {"1.70948E+00", "1.417009"}, 
         {"1.71698E+00", "1.417004"}, 
         {"1.72452E+00", "1.416998"}, 
         {"1.73209E+00", "1.416992"}, 
         {"1.73969E+00", "1.416987"}, 
         {"1.74733E+00", "1.416981"}, 
         {"1.75500E+00", "1.416975"}, 
         {"1.76271E+00", "1.416970"}, 
         {"1.77045E+00", "1.416965"}, 
         {"1.77822E+00", "1.416960"}, 
         {"1.78602E+00", "1.416955"}, 
         {"1.79386E+00", "1.416950"}, 
         {"1.80174E+00", "1.416944"}, 
         {"1.80965E+00", "1.416939"}, 
         {"1.81759E+00", "1.416934"}, 
         {"1.82557E+00", "1.416929"}, 
         {"1.83359E+00", "1.416924"}, 
         {"1.84163E+00", "1.416919"}, 
         {"1.84972E+00", "1.416914"}, 
         {"1.85784E+00", "1.416909"}, 
         {"1.86600E+00", "1.416904"}, 
         {"1.87419E+00", "1.416900"}, 
         {"1.88241E+00", "1.416895"}, 
         {"1.89068E+00", "1.416890"}, 
         {"1.89898E+00", "1.416885"}, 
         {"1.90731E+00", "1.416881"}, 
         {"1.91569E+00", "1.416877"}, 
         {"1.92410E+00", "1.416872"}, 
         {"1.93254E+00", "1.416868"}, 
         {"1.94103E+00", "1.416863"}, 
         {"1.94955E+00", "1.416859"}, 
         {"1.95811E+00", "1.416854"}, 
         {"1.96670E+00", "1.416850"}, 
         {"1.97534E+00", "1.416846"}, 
         {"1.98401E+00", "1.416842"}, 
         {"1.99272E+00", "1.416837"}, 
         {"2.00146E+00", "1.416833"}});

    return model;
  }

  public static Model run5(Model model) {
    model.component("comp1").material("mat7").propertyGroup("RefractiveIndex").func("int1")
         .set("fununit", new String[]{"1"});
    model.component("comp1").material("mat7").propertyGroup("RefractiveIndex").func("int1")
         .set("argunit", new String[]{"um"});
    model.component("comp1").material("mat7").propertyGroup("RefractiveIndex").func("int2").set("funcname", "ni");

    return model;
  }

  public static Model run6(Model model) {
    model.component("comp1").material("mat7").propertyGroup("RefractiveIndex").func("int2")
         .set("table", new String[][]{{"2.99919E-02", "0.121265"}, 
         {"3.01236E-02", "0.121903"}, 
         {"3.02558E-02", "0.122539"}, 
         {"3.03887E-02", "0.124188"}, 
         {"3.05221E-02", "0.125917"}, 
         {"3.06560E-02", "0.127636"}, 
         {"3.07906E-02", "0.129346"}, 
         {"3.09258E-02", "0.131045"}, 
         {"3.10616E-02", "0.132734"}, 
         {"3.11979E-02", "0.134413"}, 
         {"3.13349E-02", "0.136081"}, 
         {"3.14724E-02", "0.137738"}, 
         {"3.16106E-02", "0.139383"}, 
         {"3.17494E-02", "0.141018"}, 
         {"3.18887E-02", "0.142641"}, 
         {"3.20287E-02", "0.144252"}, 
         {"3.21693E-02", "0.145852"}, 
         {"3.23105E-02", "0.147439"}, 
         {"3.24524E-02", "0.149015"}, 
         {"3.25948E-02", "0.150578"}, 
         {"3.27379E-02", "0.152128"}, 
         {"3.28817E-02", "0.153666"}, 
         {"3.30260E-02", "0.155192"}, 
         {"3.31710E-02", "0.156705"}, 
         {"3.33166E-02", "0.158205"}, 
         {"3.34629E-02", "0.159691"}, 
         {"3.36098E-02", "0.161165"}, 
         {"3.37573E-02", "0.162626"}, 
         {"3.39055E-02", "0.164073"}, 
         {"3.40543E-02", "0.165507"}, 
         {"3.42038E-02", "0.166928"}, 
         {"3.43540E-02", "0.168335"}, 
         {"3.45048E-02", "0.169729"}, 
         {"3.46563E-02", "0.171108"}, 
         {"3.48084E-02", "0.172474"}, 
         {"3.49612E-02", "0.173826"}, 
         {"3.51147E-02", "0.175164"}, 
         {"3.52688E-02", "0.176488"}, 
         {"3.54237E-02", "0.177797"}, 
         {"3.55792E-02", "0.179092"}, 
         {"3.57354E-02", "0.180373"}, 
         {"3.58922E-02", "0.181638"}, 
         {"3.60498E-02", "0.182890"}, 
         {"3.62080E-02", "0.184126"}, 
         {"3.63670E-02", "0.185347"}, 
         {"3.65266E-02", "0.186552"}, 
         {"3.66870E-02", "0.187742"}, 
         {"3.68480E-02", "0.188917"}, 
         {"3.70098E-02", "0.190076"}, 
         {"3.71723E-02", "0.191218"}, 
         {"3.73355E-02", "0.192344"}, 
         {"3.74994E-02", "0.193454"}, 
         {"3.76640E-02", "0.194547"}, 
         {"3.78293E-02", "0.195622"}, 
         {"3.79954E-02", "0.196680"}, 
         {"3.81622E-02", "0.197720"}, 
         {"3.83297E-02", "0.198742"}, 
         {"3.84980E-02", "0.199746"}, 
         {"3.86670E-02", "0.200730"}, 
         {"3.88367E-02", "0.201694"}, 
         {"3.90072E-02", "0.202638"}, 
         {"3.91784E-02", "0.203561"}, 
         {"3.93504E-02", "0.204462"}, 
         {"3.95232E-02", "0.205341"}, 
         {"3.96967E-02", "0.206197"}, 
         {"3.98709E-02", "0.207028"}, 
         {"4.00460E-02", "0.207833"}, 
         {"4.02218E-02", "0.208611"}, 
         {"4.03983E-02", "0.209358"}, 
         {"4.05757E-02", "0.210073"}, 
         {"4.07538E-02", "0.210773"}, 
         {"4.09327E-02", "0.211506"}, 
         {"4.11124E-02", "0.212223"}, 
         {"4.12929E-02", "0.212913"}, 
         {"4.14742E-02", "0.213575"}, 
         {"4.16562E-02", "0.214204"}, 
         {"4.18391E-02", "0.214797"}, 
         {"4.20228E-02", "0.215351"}, 
         {"4.22072E-02", "0.215863"}, 
         {"4.23925E-02", "0.216329"}, 
         {"4.25786E-02", "0.216745"}, 
         {"4.27655E-02", "0.217104"}, 
         {"4.29533E-02", "0.217403"}, 
         {"4.31418E-02", "0.217633"}, 
         {"4.33312E-02", "0.217786"}, 
         {"4.35214E-02", "0.217852"}, 
         {"4.37125E-02", "0.217816"}, 
         {"4.39044E-02", "0.217661"}, 
         {"4.40971E-02", "0.217359"}, 
         {"4.42907E-02", "0.216870"}, 
         {"4.44851E-02", "0.216115"}, 
         {"4.46804E-02", "0.214887"}, 
         {"4.48766E-02", "0.215072"}, 
         {"4.50736E-02", "0.215889"}, 
         {"4.52714E-02", "0.216823"}, 
         {"4.54702E-02", "0.217699"}, 
         {"4.56698E-02", "0.218403"}, 
         {"4.58703E-02", "0.218791"}, 
         {"4.60716E-02", "0.218540"}, 
         {"4.62739E-02", "0.219177"}, 
         {"4.64770E-02", "0.222152"}, 
         {"4.66811E-02", "0.226280"}, 
         {"4.68860E-02", "0.230786"}, 
         {"4.70918E-02", "0.235449"}, 
         {"4.72985E-02", "0.240155"}, 
         {"4.75062E-02", "0.244826"}, 
         {"4.77147E-02", "0.249392"}, 
         {"4.79242E-02", "0.253783"}, 
         {"4.81346E-02", "0.257913"}, 
         {"4.83459E-02", "0.261662"}, 
         {"4.85581E-02", "0.264841"}, 
         {"4.87713E-02", "0.267079"}, 
         {"4.89854E-02", "0.267319"}, 
         {"4.92004E-02", "0.271658"}, 
         {"4.94164E-02", "0.281446"}, 
         {"4.96333E-02", "0.293601"}, 
         {"4.98512E-02", "0.306861"}, 
         {"5.00701E-02", "0.320765"}, 
         {"5.02899E-02", "0.335065"}, 
         {"5.05106E-02", "0.349557"}, 
         {"5.07324E-02", "0.363928"}, 
         {"5.09551E-02", "0.378138"}, 
         {"5.11788E-02", "0.399372"}, 
         {"5.14034E-02", "0.422188"}, 
         {"5.16291E-02", "0.436463"}, 
         {"5.18557E-02", "0.453521"}, 
         {"5.20834E-02", "0.478656"}, 
         {"5.23120E-02", "0.502338"}, 
         {"5.25417E-02", "0.521008"}, 
         {"5.27723E-02", "0.537604"}, 
         {"5.30040E-02", "0.553337"}, 
         {"5.32367E-02", "0.568700"}, 
         {"5.34704E-02", "0.584091"}, 
         {"5.37051E-02", "0.598995"}, 
         {"5.39409E-02", "0.611902"}, 
         {"5.41777E-02", "0.622974"}, 
         {"5.44155E-02", "0.633135"}, 
         {"5.46544E-02", "0.642653"}, 
         {"5.48943E-02", "0.651655"}, 
         {"5.51353E-02", "0.660221"}, 
         {"5.53773E-02", "0.668406"}, 
         {"5.56204E-02", "0.676253"}, 
         {"5.58646E-02", "0.683795"}, 
         {"5.61098E-02", "0.691059"}, 
         {"5.63562E-02", "0.698066"}, 
         {"5.66036E-02", "0.704838"}, 
         {"5.68520E-02", "0.711389"}, 
         {"5.71016E-02", "0.717734"}, 
         {"5.73523E-02", "0.723883"}, 
         {"5.76041E-02", "0.729824"}, 
         {"5.78569E-02", "0.735579"}, 
         {"5.81109E-02", "0.741165"}, 
         {"5.83660E-02", "0.746594"}, 
         {"5.86222E-02", "0.751876"}, 
         {"5.88796E-02", "0.757024"}, 
         {"5.91381E-02", "0.762054"}, 
         {"5.93977E-02", "0.766991"}, 
         {"5.96584E-02", "0.771885"}, 
         {"5.99203E-02", "0.776710"}, 
         {"6.01834E-02", "0.779506"}, 
         {"6.04476E-02", "0.779711"}, 
         {"6.07129E-02", "0.779183"}, 
         {"6.09794E-02", "0.778053"}, 
         {"6.12471E-02", "0.776296"}, 
         {"6.15160E-02", "0.777796"}, 
         {"6.17861E-02", "0.780571"}, 
         {"6.20573E-02", "0.783446"}, 
         {"6.23297E-02", "0.786333"}, 
         {"6.26033E-02", "0.789214"}, 
         {"6.28782E-02", "0.792139"}, 
         {"6.31542E-02", "0.791198"}, 
         {"6.34314E-02", "0.789496"}, 
         {"6.37099E-02", "0.787433"}, 
         {"6.39896E-02", "0.785068"}, 
         {"6.42705E-02", "0.782429"}, 
         {"6.45526E-02", "0.779538"}, 
         {"6.48360E-02", "0.776409"}, 
         {"6.51206E-02", "0.773056"}, 
         {"6.54065E-02", "0.769487"}, 
         {"6.56936E-02", "0.765712"}, 
         {"6.59820E-02", "0.761735"}, 
         {"6.62717E-02", "0.757436"}, 
         {"6.65626E-02", "0.752602"}, 
         {"6.68548E-02", "0.747501"}, 
         {"6.71483E-02", "0.742172"}, 
         {"6.74431E-02", "0.736102"}, 
         {"6.77391E-02", "0.729683"}, 
         {"6.80365E-02", "0.722219"}, 
         {"6.83352E-02", "0.713819"}, 
         {"6.86352E-02", "0.705249"}, 
         {"6.89365E-02", "0.696783"}, 
         {"6.92391E-02", "0.688490"}, 
         {"6.95430E-02", "0.680494"}, 
         {"6.98483E-02", "0.673261"}, 
         {"7.01550E-02", "0.671523"}, 
         {"7.04629E-02", "0.672269"}, 
         {"7.07723E-02", "0.672676"}, 
         {"7.10829E-02", "0.672939"}, 
         {"7.13950E-02", "0.673201"}, 
         {"7.17084E-02", "0.673776"}, 
         {"7.20232E-02", "0.681587"}, 
         {"7.23394E-02", "0.691890"}, 
         {"7.26569E-02", "0.693496"}, 
         {"7.29759E-02", "0.689920"}, 
         {"7.32962E-02", "0.686483"}, 
         {"7.36180E-02", "0.682902"}, 
         {"7.39412E-02", "0.678979"}, 
         {"7.42658E-02", "0.674005"}, 
         {"7.45918E-02", "0.665948"}, 
         {"7.49193E-02", "0.655694"}, 
         {"7.52481E-02", "0.645833"}, 
         {"7.55785E-02", "0.636216"}, 
         {"7.59103E-02", "0.626804"}, 
         {"7.62435E-02", "0.617591"}, 
         {"7.65782E-02", "0.608593"}, 
         {"7.69144E-02", "0.599843"}, 
         {"7.72520E-02", "0.591411"}, 
         {"7.75911E-02", "0.583570"}, 
         {"7.79318E-02", "0.577077"}, 
         {"7.82739E-02", "0.571832"}, 
         {"7.86175E-02", "0.566353"}, 
         {"7.89626E-02", "0.560782"}, 
         {"7.93092E-02", "0.555171"}, 
         {"7.96574E-02", "0.549546"}, 
         {"8.00071E-02", "0.543925"}, 
         {"8.03583E-02", "0.538318"}, 
         {"8.07111E-02", "0.532735"}, 
         {"8.10654E-02", "0.527179"}, 
         {"8.14213E-02", "0.521653"}, 
         {"8.17787E-02", "0.516154"}, 
         {"8.21377E-02", "0.510664"}, 
         {"8.24983E-02", "0.505085"}, 
         {"8.28604E-02", "0.499208"}, 
         {"8.32242E-02", "0.493281"}, 
         {"8.35895E-02", "0.487919"}, 
         {"8.39565E-02", "0.483356"}, 
         {"8.43250E-02", "0.478763"}, 
         {"8.46952E-02", "0.474220"}, 
         {"8.50670E-02", "0.469755"}, 
         {"8.54405E-02", "0.465379"}, 
         {"8.58155E-02", "0.461091"}, 
         {"8.61923E-02", "0.456861"}, 
         {"8.65706E-02", "0.452539"}, 
         {"8.69507E-02", "0.447532"}, 
         {"8.73324E-02", "0.442340"}, 
         {"8.77158E-02", "0.438345"}, 
         {"8.81008E-02", "0.435819"}, 
         {"8.84876E-02", "0.436086"}, 
         {"8.88760E-02", "0.446266"}, 
         {"8.92662E-02", "0.459205"}, 
         {"8.96581E-02", "0.453815"}, 
         {"9.00517E-02", "0.442449"}, 
         {"9.04470E-02", "0.437260"}, 
         {"9.08440E-02", "0.446421"}, 
         {"9.12428E-02", "0.452922"}, 
         {"9.16434E-02", "0.455904"}, 
         {"9.20457E-02", "0.453592"}, 
         {"9.24497E-02", "0.439965"}, 
         {"9.28556E-02", "0.427591"}, 
         {"9.32632E-02", "0.415678"}, 
         {"9.36726E-02", "0.401269"}, 
         {"9.40838E-02", "0.390121"}, 
         {"9.44969E-02", "0.383402"}, 
         {"9.49117E-02", "0.382669"}, 
         {"9.53284E-02", "0.392156"}, 
         {"9.57468E-02", "0.395446"}, 
         {"9.61672E-02", "0.395566"}, 
         {"9.65893E-02", "0.394990"}, 
         {"9.70133E-02", "0.390313"}, 
         {"9.74392E-02", "0.377165"}, 
         {"9.78670E-02", "0.384131"}, 
         {"9.82966E-02", "0.411995"}, 
         {"9.87281E-02", "0.430270"}, 
         {"9.91615E-02", "0.438304"}, 
         {"9.95968E-02", "0.461196"}, 
         {"1.00034E-01", "0.504175"}, 
         {"1.00473E-01", "0.561164"}, 
         {"1.00914E-01", "0.608032"}, 
         {"1.01357E-01", "0.653320"}, 
         {"1.01802E-01", "0.693004"}, 
         {"1.02249E-01", "0.740010"}, 
         {"1.02698E-01", "0.791328"}, 
         {"1.03149E-01", "0.838845"}, 
         {"1.03602E-01", "0.898753"}, 
         {"1.04056E-01", "0.958900"}, 
         {"1.04513E-01", "0.972986"}, 
         {"1.04972E-01", "0.887666"}, 
         {"1.05433E-01", "0.750410"}, 
         {"1.05896E-01", "0.616078"}, 
         {"1.06361E-01", "0.530400"}, 
         {"1.06827E-01", "0.492356"}, 
         {"1.07296E-01", "0.459934"}, 
         {"1.07767E-01", "0.423267"}, 
         {"1.08241E-01", "0.358978"}, 
         {"1.08716E-01", "0.267565"}, 
         {"1.09193E-01", "0.178498"}, 
         {"1.09672E-01", "0.131735"}, 
         {"1.10154E-01", "0.124047"}, 
         {"1.10637E-01", "0.116392"}, 
         {"1.11123E-01", "0.108771"}, 
         {"1.11611E-01", "0.101183"}, 
         {"1.12101E-01", "0.093629"}, 
         {"1.12593E-01", "0.086107"}, 
         {"1.13087E-01", "0.078618"}, 
         {"1.13584E-01", "0.071337"}, 
         {"1.14082E-01", "0.064950"}, 
         {"1.14583E-01", "0.058591"}, 
         {"1.15086E-01", "0.052260"}, 
         {"1.15591E-01", "0.045956"}, 
         {"1.16099E-01", "0.039680"}, 
         {"1.16608E-01", "0.033503"}, 
         {"1.17120E-01", "0.031640"}, 
         {"1.17634E-01", "0.029786"}, 
         {"1.18151E-01", "0.027940"}, 
         {"1.18669E-01", "0.026102"}, 
         {"1.19190E-01", "0.024272"}, 
         {"1.19714E-01", "0.022941"}, 
         {"1.20239E-01", "0.021625"}, 
         {"1.20767E-01", "0.020315"}, 
         {"1.21297E-01", "0.019010"}, 
         {"1.21830E-01", "0.017978"}, 
         {"1.22364E-01", "0.017231"}, 
         {"1.22902E-01", "0.016486"}, 
         {"1.23441E-01", "0.015745"}, 
         {"1.23983E-01", "0.013826"}, 
         {"1.24527E-01", "0.011427"}, 
         {"1.25074E-01", "0.009037"}, 
         {"1.25623E-01", "0.008738"}, 
         {"1.26175E-01", "0.008544"}, 
         {"1.26728E-01", "0.008351"}, 
         {"1.27285E-01", "0.008158"}, 
         {"1.27844E-01", "0.007966"}, 
         {"1.28405E-01", "0.007775"}, 
         {"1.28968E-01", "0.007585"}, 
         {"1.29535E-01", "0.007396"}, 
         {"1.30103E-01", "0.007279"}, 
         {"1.30674E-01", "0.007171"}, 
         {"1.31248E-01", "0.007064"}, 
         {"1.31824E-01", "0.006958"}, 
         {"1.32403E-01", "0.006851"}, 
         {"1.32984E-01", "0.006746"}, 
         {"1.33568E-01", "0.006619"}, 
         {"1.34154E-01", "0.006439"}, 
         {"1.34743E-01", "0.006260"}, 
         {"1.35335E-01", "0.006082"}, 
         {"1.35929E-01", "0.005904"}, 
         {"1.36526E-01", "0.005725"}, 
         {"1.37125E-01", "0.005547"}, 
         {"1.37727E-01", "0.005380"}, 
         {"1.38331E-01", "0.005527"}, 
         {"1.38939E-01", "0.005673"}, 
         {"1.39549E-01", "0.005819"}, 
         {"1.40161E-01", "0.005851"}, 
         {"1.40777E-01", "0.005798"}, 
         {"1.41395E-01", "0.005664"}, 
         {"1.42015E-01", "0.005460"}, 
         {"1.42639E-01", "0.005258"}, 
         {"1.43265E-01", "0.005056"}, 
         {"1.43894E-01", "0.004881"}, 
         {"1.44525E-01", "0.004884"}, 
         {"1.45160E-01", "0.004887"}, 
         {"1.45797E-01", "0.004890"}, 
         {"1.46437E-01", "0.004893"}, 
         {"1.47080E-01", "0.004881"}, 
         {"1.47726E-01", "0.004813"}, 
         {"1.48374E-01", "0.004744"}, 
         {"1.49026E-01", "0.004677"}, 
         {"1.49680E-01", "0.004609"}, 
         {"1.50337E-01", "0.004542"}, 
         {"1.50997E-01", "0.004475"}, 
         {"1.51660E-01", "0.004409"}, 
         {"1.52325E-01", "0.004378"}, 
         {"1.52994E-01", "0.004348"}, 
         {"1.53666E-01", "0.004318"}, 
         {"1.54340E-01", "0.004289"}, 
         {"1.55018E-01", "0.004259"}, 
         {"1.55698E-01", "0.004229"}, 
         {"1.56382E-01", "0.004169"}, 
         {"1.57068E-01", "0.004018"}, 
         {"1.57758E-01", "0.003868"}, 
         {"1.58450E-01", "0.003718"}, 
         {"1.59146E-01", "0.003569"}, 
         {"1.59845E-01", "0.003421"}, 
         {"1.60546E-01", "0.003289"}, 
         {"1.61251E-01", "0.003161"}, 
         {"1.61959E-01", "0.003033"}, 
         {"1.62670E-01", "0.002906"}, 
         {"1.63384E-01", "0.002795"}, 
         {"1.64101E-01", "0.002762"}, 
         {"1.64822E-01", "0.002729"}, 
         {"1.65545E-01", "0.002697"}, 
         {"1.66272E-01", "0.002665"}, 
         {"1.67002E-01", "0.002633"}, 
         {"1.67735E-01", "0.002601"}, 
         {"1.68471E-01", "0.002569"}, 
         {"1.69211E-01", "0.002537"}, 
         {"1.69954E-01", "0.002506"}, 
         {"1.70700E-01", "0.002475"}, 
         {"1.71449E-01", "0.002443"}, 
         {"1.72202E-01", "0.002412"}, 
         {"1.72958E-01", "0.002381"}, 
         {"1.73717E-01", "0.002350"}, 
         {"1.74480E-01", "0.002320"}, 
         {"1.75246E-01", "0.002289"}, 
         {"1.76015E-01", "0.002257"}, 
         {"1.76788E-01", "0.002226"}, 
         {"1.77564E-01", "0.002195"}, 
         {"1.78343E-01", "0.002164"}, 
         {"1.79126E-01", "0.002133"}, 
         {"1.79912E-01", "0.002103"}, 
         {"1.80702E-01", "0.002072"}, 
         {"1.81496E-01", "0.002041"}, 
         {"1.82292E-01", "0.002011"}, 
         {"1.83093E-01", "0.002000"}, 
         {"1.83896E-01", "0.001990"}, 
         {"1.84704E-01", "0.001980"}, 
         {"1.85514E-01", "0.001970"}, 
         {"1.86329E-01", "0.001960"}, 
         {"1.87147E-01", "0.001950"}, 
         {"1.87968E-01", "0.001941"}, 
         {"1.88793E-01", "0.001930"}, 
         {"1.89622E-01", "0.001921"}, 
         {"1.90455E-01", "0.001912"}, 
         {"1.91291E-01", "0.001902"}, 
         {"1.92131E-01", "0.001893"}, 
         {"1.92974E-01", "0.001883"}, 
         {"1.93821E-01", "0.001873"}, 
         {"1.94672E-01", "0.001864"}, 
         {"1.95527E-01", "0.001855"}, 
         {"1.96385E-01", "0.001846"}, 
         {"1.97247E-01", "0.001837"}, 
         {"1.98113E-01", "0.001827"}, 
         {"1.98983E-01", "0.001818"}, 
         {"1.99856E-01", "0.001809"}, 
         {"2.00733E-01", "0.001800"}, 
         {"2.01615E-01", "0.001791"}, 
         {"2.02500E-01", "0.001783"}, 
         {"2.03389E-01", "0.001774"}, 
         {"2.04282E-01", "0.001766"}, 
         {"2.05178E-01", "0.001757"}, 
         {"2.06079E-01", "0.001748"}, 
         {"2.06984E-01", "0.001739"}, 
         {"2.07892E-01", "0.001731"}, 
         {"2.08805E-01", "0.001723"}, 
         {"2.09722E-01", "0.001714"}, 
         {"2.10642E-01", "0.001706"}, 
         {"2.11567E-01", "0.001697"}, 
         {"2.12496E-01", "0.001690"}, 
         {"2.13429E-01", "0.001681"}, 
         {"2.14365E-01", "0.001673"}, 
         {"2.15307E-01", "0.001665"}, 
         {"2.16252E-01", "0.001657"}, 
         {"2.17201E-01", "0.001649"}, 
         {"2.18155E-01", "0.001640"}, 
         {"2.19112E-01", "0.001633"}, 
         {"2.20074E-01", "0.001625"}, 
         {"2.21040E-01", "0.001617"}, 
         {"2.22011E-01", "0.001609"}, 
         {"2.22985E-01", "0.001602"}, 
         {"2.23964E-01", "0.001594"}, 
         {"2.24947E-01", "0.001586"}, 
         {"2.25935E-01", "0.001579"}, 
         {"2.26927E-01", "0.001571"}, 
         {"2.27923E-01", "0.001563"}, 
         {"2.28923E-01", "0.001555"}, 
         {"2.29928E-01", "0.001549"}, 
         {"2.30938E-01", "0.001541"}, 
         {"2.31951E-01", "0.001534"}, 
         {"2.32970E-01", "0.001526"}, 
         {"2.33992E-01", "0.001519"}, 
         {"2.35020E-01", "0.001512"}, 
         {"2.36051E-01", "0.001504"}, 
         {"2.37088E-01", "0.001498"}, 
         {"2.38128E-01", "0.001490"}, 
         {"2.39174E-01", "0.001483"}, 
         {"2.40224E-01", "0.001476"}, 
         {"2.41278E-01", "0.001469"}, 
         {"2.42337E-01", "0.001462"}, 
         {"2.43401E-01", "0.001455"}, 
         {"2.44470E-01", "0.001448"}, 
         {"2.45543E-01", "0.001442"}, 
         {"2.46621E-01", "0.001434"}, 
         {"2.47703E-01", "0.001428"}, 
         {"2.48791E-01", "0.001421"}, 
         {"2.49883E-01", "0.001414"}, 
         {"2.50980E-01", "0.001407"}, 
         {"2.52082E-01", "0.001401"}, 
         {"2.53188E-01", "0.001395"}, 
         {"2.54300E-01", "0.001387"}, 
         {"2.55416E-01", "0.001381"}, 
         {"2.56537E-01", "0.001375"}, 
         {"2.57664E-01", "0.001368"}, 
         {"2.58795E-01", "0.001361"}, 
         {"2.59931E-01", "0.001355"}, 
         {"2.61072E-01", "0.001349"}, 
         {"2.62218E-01", "0.001343"}, 
         {"2.63369E-01", "0.001336"}, 
         {"2.64525E-01", "0.001330"}, 
         {"2.65687E-01", "0.001324"}, 
         {"2.66853E-01", "0.001318"}, 
         {"2.68024E-01", "0.001312"}, 
         {"2.69201E-01", "0.001305"}, 
         {"2.70383E-01", "0.001299"}, 
         {"2.71570E-01", "0.001293"}, 
         {"2.72762E-01", "0.001287"}, 
         {"2.73959E-01", "0.001281"}, 
         {"2.75162E-01", "0.001276"}, 
         {"2.76370E-01", "0.001269"}, 
         {"2.77583E-01", "0.001263"}, 
         {"2.78802E-01", "0.001257"}, 
         {"2.80025E-01", "0.001252"}, 
         {"2.81255E-01", "0.001246"}, 
         {"2.82489E-01", "0.001240"}, 
         {"2.83730E-01", "0.001234"}, 
         {"2.84975E-01", "0.001229"}, 
         {"2.86226E-01", "0.001222"}, 
         {"2.87483E-01", "0.001217"}, 
         {"2.88745E-01", "0.001211"}, 
         {"2.90012E-01", "0.001206"}, 
         {"2.91285E-01", "0.001200"}, 
         {"2.92564E-01", "0.001195"}, 
         {"2.93848E-01", "0.001189"}, 
         {"2.95138E-01", "0.001184"}, 
         {"2.96434E-01", "0.001178"}, 
         {"2.97735E-01", "0.001173"}, 
         {"2.99042E-01", "0.001168"}, 
         {"3.00355E-01", "0.001162"}, 
         {"3.01674E-01", "0.001157"}, 
         {"3.02998E-01", "0.001151"}, 
         {"3.04328E-01", "0.001145"}, 
         {"3.05664E-01", "0.001140"}, 
         {"3.07006E-01", "0.001135"}, 
         {"3.08354E-01", "0.001130"}, 
         {"3.09707E-01", "0.001125"}, 
         {"3.11067E-01", "0.001119"}, 
         {"3.12432E-01", "0.001114"}, 
         {"3.13804E-01", "0.001109"}, 
         {"3.15182E-01", "0.001104"}, 
         {"3.16565E-01", "0.001099"}, 
         {"3.17955E-01", "0.001094"}, 
         {"3.19351E-01", "0.001089"}, 
         {"3.20753E-01", "0.001084"}, 
         {"3.22161E-01", "0.001079"}, 
         {"3.23575E-01", "0.001074"}, 
         {"3.24995E-01", "0.001069"}, 
         {"3.26422E-01", "0.001064"}, 
         {"3.27855E-01", "0.001059"}, 
         {"3.29294E-01", "0.001054"}, 
         {"3.30740E-01", "0.001050"}, 
         {"3.32192E-01", "0.001045"}, 
         {"3.33650E-01", "0.001040"}, 
         {"3.35115E-01", "0.001035"}, 
         {"3.36586E-01", "0.001030"}, 
         {"3.38063E-01", "0.001026"}, 
         {"3.39547E-01", "0.001022"}, 
         {"3.41038E-01", "0.001017"}, 
         {"3.42535E-01", "0.001013"}, 
         {"3.44039E-01", "0.001008"}, 
         {"3.45549E-01", "0.001003"}, 
         {"3.47066E-01", "0.000999"}, 
         {"3.48590E-01", "0.000994"}, 
         {"3.50120E-01", "0.000990"}, 
         {"3.51657E-01", "0.000985"}, 
         {"3.53201E-01", "0.000980"}, 
         {"3.54751E-01", "0.000976"}, 
         {"3.56309E-01", "0.000971"}, 
         {"3.57873E-01", "0.000967"}, 
         {"3.59444E-01", "0.000963"}, 
         {"3.61022E-01", "0.000958"}, 
         {"3.62607E-01", "0.000954"}, 
         {"3.64198E-01", "0.000949"}, 
         {"3.65797E-01", "0.000945"}, 
         {"3.67403E-01", "0.000942"}, 
         {"3.69016E-01", "0.000937"}, 
         {"3.70636E-01", "0.000933"}, 
         {"3.72263E-01", "0.000929"}, 
         {"3.73897E-01", "0.000924"}, 
         {"3.75538E-01", "0.000920"}, 
         {"3.77187E-01", "0.000916"}, 
         {"3.78843E-01", "0.000912"}, 
         {"3.80506E-01", "0.000907"}, 
         {"3.82176E-01", "0.000903"}, 
         {"3.83854E-01", "0.000899"}, 
         {"3.85539E-01", "0.000895"}, 
         {"3.87232E-01", "0.000892"}, 
         {"3.88931E-01", "0.000888"}, 
         {"3.90639E-01", "0.000883"}, 
         {"3.92354E-01", "0.000879"}, 
         {"3.94076E-01", "0.000875"}, 
         {"3.95806E-01", "0.000871"}, 
         {"3.97544E-01", "0.000867"}, 
         {"3.99289E-01", "0.000863"}, 
         {"4.01042E-01", "0.000859"}, 
         {"4.02802E-01", "0.000855"}, 
         {"4.04570E-01", "0.000852"}, 
         {"4.06346E-01", "0.000848"}, 
         {"4.08130E-01", "0.000844"}, 
         {"4.09922E-01", "0.000840"}, 
         {"4.11721E-01", "0.000837"}, 
         {"4.13529E-01", "0.000833"}, 
         {"4.15344E-01", "0.000829"}, 
         {"4.17167E-01", "0.000825"}, 
         {"4.18999E-01", "0.000821"}, 
         {"4.20838E-01", "0.000818"}, 
         {"4.22686E-01", "0.000815"}, 
         {"4.24541E-01", "0.000811"}, 
         {"4.26405E-01", "0.000807"}, 
         {"4.28277E-01", "0.000803"}, 
         {"4.30157E-01", "0.000800"}, 
         {"4.32045E-01", "0.000796"}, 
         {"4.33942E-01", "0.000792"}, 
         {"4.35847E-01", "0.000789"}, 
         {"4.37760E-01", "0.000786"}, 
         {"4.39682E-01", "0.000782"}, 
         {"4.41612E-01", "0.000779"}, 
         {"4.43551E-01", "0.000775"}, 
         {"4.45498E-01", "0.000771"}, 
         {"4.47453E-01", "0.000768"}, 
         {"4.49418E-01", "0.000764"}, 
         {"4.51391E-01", "0.000761"}, 
         {"4.53372E-01", "0.000758"}, 
         {"4.55362E-01", "0.000755"}, 
         {"4.57361E-01", "0.000751"}, 
         {"4.59369E-01", "0.000748"}, 
         {"4.61386E-01", "0.000744"}, 
         {"4.63411E-01", "0.000741"}, 
         {"4.65446E-01", "0.000737"}, 
         {"4.67489E-01", "0.000735"}, 
         {"4.69541E-01", "0.000731"}, 
         {"4.71602E-01", "0.000728"}, 
         {"4.73673E-01", "0.000725"}, 
         {"4.75752E-01", "0.000721"}, 
         {"4.77841E-01", "0.000718"}, 
         {"4.79938E-01", "0.000715"}, 
         {"4.82045E-01", "0.000711"}, 
         {"4.84161E-01", "0.000709"}, 
         {"4.86287E-01", "0.000706"}, 
         {"4.88421E-01", "0.000702"}, 
         {"4.90566E-01", "0.000699"}, 
         {"4.92719E-01", "0.000696"}, 
         {"4.94882E-01", "0.000693"}, 
         {"4.97055E-01", "0.000689"}, 
         {"4.99237E-01", "0.000687"}, 
         {"5.01428E-01", "0.000684"}, 
         {"5.03629E-01", "0.000681"}, 
         {"5.05840E-01", "0.000678"}, 
         {"5.08061E-01", "0.000674"}, 
         {"5.10291E-01", "0.000671"}, 
         {"5.12531E-01", "0.000668"}, 
         {"5.14781E-01", "0.000665"}, 
         {"5.17041E-01", "0.000663"}, 
         {"5.19311E-01", "0.000660"}, 
         {"5.21591E-01", "0.000657"}, 
         {"5.23880E-01", "0.000654"}, 
         {"5.26180E-01", "0.000651"}, 
         {"5.28490E-01", "0.000648"}, 
         {"5.30810E-01", "0.000645"}, 
         {"5.33140E-01", "0.000643"}, 
         {"5.35481E-01", "0.000640"}, 
         {"5.37831E-01", "0.000637"}, 
         {"5.40192E-01", "0.000634"}, 
         {"5.42564E-01", "0.000631"}, 
         {"5.44946E-01", "0.000628"}, 
         {"5.47338E-01", "0.000625"}, 
         {"5.49741E-01", "0.000622"}, 
         {"5.52154E-01", "0.000620"}, 
         {"5.54578E-01", "0.000617"}, 
         {"5.57012E-01", "0.000614"}, 
         {"5.59458E-01", "0.000611"}, 
         {"5.61914E-01", "0.000609"}, 
         {"5.64380E-01", "0.000606"}, 
         {"5.66858E-01", "0.000603"}, 
         {"5.69346E-01", "0.000601"}, 
         {"5.71846E-01", "0.000598"}, 
         {"5.74356E-01", "0.000596"}, 
         {"5.76877E-01", "0.000593"}, 
         {"5.79410E-01", "0.000590"}, 
         {"5.81953E-01", "0.000587"}, 
         {"5.84508E-01", "0.000585"}, 
         {"5.87074E-01", "0.000583"}, 
         {"5.89651E-01", "0.000580"}, 
         {"5.92240E-01", "0.000577"}, 
         {"5.94840E-01", "0.000575"}, 
         {"5.97451E-01", "0.000572"}, 
         {"6.00074E-01", "0.000569"}, 
         {"6.02708E-01", "0.000567"}, 
         {"6.05354E-01", "0.000564"}, 
         {"6.08011E-01", "0.000562"}, 
         {"6.10680E-01", "0.000560"}, 
         {"6.13361E-01", "0.000557"}, 
         {"6.16054E-01", "0.000555"}, 
         {"6.18758E-01", "0.000552"}, 
         {"6.21475E-01", "0.000549"}, 
         {"6.24203E-01", "0.000547"}, 
         {"6.26943E-01", "0.000545"}, 
         {"6.29695E-01", "0.000543"}, 
         {"6.32459E-01", "0.000540"}, 
         {"6.35236E-01", "0.000538"}, 
         {"6.38025E-01", "0.000535"}, 
         {"6.40825E-01", "0.000533"}, 
         {"6.43639E-01", "0.000530"}, 
         {"6.46464E-01", "0.000528"}, 
         {"6.49302E-01", "0.000526"}, 
         {"6.52152E-01", "0.000524"}, 
         {"6.55015E-01", "0.000521"}, 
         {"6.57891E-01", "0.000519"}, 
         {"6.60779E-01", "0.000516"}, 
         {"6.63680E-01", "0.000514"}, 
         {"6.66593E-01", "0.000512"}, 
         {"6.69519E-01", "0.000510"}, 
         {"6.72458E-01", "0.000508"}, 
         {"6.75410E-01", "0.000505"}, 
         {"6.78375E-01", "0.000503"}, 
         {"6.81353E-01", "0.000501"}, 
         {"6.84345E-01", "0.000498"}, 
         {"6.87349E-01", "0.000496"}, 
         {"6.90366E-01", "0.000494"}, 
         {"6.93397E-01", "0.000492"}, 
         {"6.96441E-01", "0.000490"}, 
         {"6.99498E-01", "0.000488"}, 
         {"7.02569E-01", "0.000486"}, 
         {"7.05653E-01", "0.000483"}, 
         {"7.08751E-01", "0.000481"}, 
         {"7.11862E-01", "0.000479"}, 
         {"7.14987E-01", "0.000477"}, 
         {"7.18126E-01", "0.000475"}, 
         {"7.21278E-01", "0.000473"}, 
         {"7.24445E-01", "0.000471"}, 
         {"7.27625E-01", "0.000469"}, 
         {"7.30819E-01", "0.000466"}, 
         {"7.34027E-01", "0.000464"}, 
         {"7.37250E-01", "0.000462"}, 
         {"7.40486E-01", "0.000460"}, 
         {"7.43737E-01", "0.000459"}, 
         {"7.47002E-01", "0.000457"}, 
         {"7.50281E-01", "0.000455"}, 
         {"7.53575E-01", "0.000452"}, 
         {"7.56883E-01", "0.000450"}, 
         {"7.60205E-01", "0.000448"}, 
         {"7.63543E-01", "0.000446"}, 
         {"7.66895E-01", "0.000444"}, 
         {"7.70261E-01", "0.000443"}, 
         {"7.73642E-01", "0.000441"}, 
         {"7.77039E-01", "0.000439"}, 
         {"7.80450E-01", "0.000437"}, 
         {"7.83876E-01", "0.000435"}, 
         {"7.87317E-01", "0.000433"}, 
         {"7.90773E-01", "0.000431"}, 
         {"7.94245E-01", "0.000429"}, 
         {"7.97731E-01", "0.000428"}, 
         {"8.01233E-01", "0.000426"}, 
         {"8.04751E-01", "0.000424"}, 
         {"8.08283E-01", "0.000422"}, 
         {"8.11832E-01", "0.000420"}, 
         {"8.15396E-01", "0.000418"}, 
         {"8.18975E-01", "0.000416"}, 
         {"8.22570E-01", "0.000414"}, 
         {"8.26181E-01", "0.000412"}, 
         {"8.29808E-01", "0.000411"}, 
         {"8.33451E-01", "0.000409"}, 
         {"8.37110E-01", "0.000407"}, 
         {"8.40785E-01", "0.000405"}, 
         {"8.44476E-01", "0.000403"}, 
         {"8.48183E-01", "0.000401"}, 
         {"8.51906E-01", "0.000400"}, 
         {"8.55646E-01", "0.000398"}, 
         {"8.59402E-01", "0.000396"}, 
         {"8.63175E-01", "0.000395"}, 
         {"8.66964E-01", "0.000393"}, 
         {"8.70770E-01", "0.000391"}, 
         {"8.74593E-01", "0.000389"}, 
         {"8.78432E-01", "0.000388"}, 
         {"8.82288E-01", "0.000386"}, 
         {"8.86161E-01", "0.000384"}, 
         {"8.90052E-01", "0.000382"}, 
         {"8.93959E-01", "0.000381"}, 
         {"8.97883E-01", "0.000380"}, 
         {"9.01825E-01", "0.000378"}, 
         {"9.05784E-01", "0.000376"}, 
         {"9.09760E-01", "0.000374"}, 
         {"9.13754E-01", "0.000373"}, 
         {"9.17765E-01", "0.000371"}, 
         {"9.21794E-01", "0.000369"}, 
         {"9.25841E-01", "0.000367"}, 
         {"9.29905E-01", "0.000366"}, 
         {"9.33987E-01", "0.000365"}, 
         {"9.38087E-01", "0.000363"}, 
         {"9.42205E-01", "0.000361"}, 
         {"9.46342E-01", "0.000360"}, 
         {"9.50496E-01", "0.000358"}, 
         {"9.54669E-01", "0.000356"}, 
         {"9.58859E-01", "0.000355"}, 
         {"9.63069E-01", "0.000353"}, 
         {"9.67296E-01", "0.000351"}, 
         {"9.71543E-01", "0.000351"}, 
         {"9.75808E-01", "0.000349"}, 
         {"9.80092E-01", "0.000347"}, 
         {"9.84394E-01", "0.000346"}, 
         {"9.88715E-01", "0.000344"}, 
         {"9.93056E-01", "0.000343"}, 
         {"9.97415E-01", "0.000341"}, 
         {"1.00179E+00", "0.000339"}, 
         {"1.00619E+00", "0.000338"}, 
         {"1.01061E+00", "0.000336"}, 
         {"1.01505E+00", "0.000336"}, 
         {"1.01950E+00", "0.000334"}, 
         {"1.02398E+00", "0.000333"}, 
         {"1.02847E+00", "0.000331"}, 
         {"1.03299E+00", "0.000329"}, 
         {"1.03752E+00", "0.000328"}, 
         {"1.04208E+00", "0.000326"}, 
         {"1.04665E+00", "0.000325"}, 
         {"1.05125E+00", "0.000323"}, 
         {"1.05586E+00", "0.000322"}, 
         {"1.06050E+00", "0.000321"}, 
         {"1.06515E+00", "0.000320"}, 
         {"1.06983E+00", "0.000318"}, 
         {"1.07452E+00", "0.000317"}, 
         {"1.07924E+00", "0.000315"}, 
         {"1.08398E+00", "0.000314"}, 
         {"1.08874E+00", "0.000312"}, 
         {"1.09352E+00", "0.000311"}, 
         {"1.09832E+00", "0.000309"}, 
         {"1.10314E+00", "0.000308"}, 
         {"1.10798E+00", "0.000306"}, 
         {"1.11284E+00", "0.000306"}, 
         {"1.11773E+00", "0.000305"}, 
         {"1.12264E+00", "0.000303"}, 
         {"1.12756E+00", "0.000302"}, 
         {"1.13251E+00", "0.000300"}, 
         {"1.13749E+00", "0.000299"}, 
         {"1.14248E+00", "0.000298"}, 
         {"1.14750E+00", "0.000296"}, 
         {"1.15253E+00", "0.000295"}, 
         {"1.15759E+00", "0.000293"}, 
         {"1.16267E+00", "0.000292"}, 
         {"1.16778E+00", "0.000292"}, 
         {"1.17290E+00", "0.000290"}, 
         {"1.17805E+00", "0.000289"}, 
         {"1.18322E+00", "0.000288"}, 
         {"1.18842E+00", "0.000286"}, 
         {"1.19364E+00", "0.000285"}, 
         {"1.19888E+00", "0.000284"}, 
         {"1.20414E+00", "0.000282"}, 
         {"1.20942E+00", "0.000281"}, 
         {"1.21473E+00", "0.000280"}, 
         {"1.22007E+00", "0.000278"}, 
         {"1.22542E+00", "0.000278"}, 
         {"1.23080E+00", "0.000277"}, 
         {"1.23621E+00", "0.000275"}, 
         {"1.24163E+00", "0.000274"}, 
         {"1.24708E+00", "0.000273"}, 
         {"1.25256E+00", "0.000271"}, 
         {"1.25806E+00", "0.000270"}, 
         {"1.26358E+00", "0.000269"}, 
         {"1.26913E+00", "0.000268"}, 
         {"1.27470E+00", "0.000266"}, 
         {"1.28029E+00", "0.000265"}, 
         {"1.28591E+00", "0.000264"}, 
         {"1.29156E+00", "0.000263"}, 
         {"1.29723E+00", "0.000262"}, 
         {"1.30292E+00", "0.000261"}, 
         {"1.30864E+00", "0.000260"}, 
         {"1.31439E+00", "0.000259"}, 
         {"1.32016E+00", "0.000258"}, 
         {"1.32595E+00", "0.000256"}, 
         {"1.33177E+00", "0.000255"}, 
         {"1.33762E+00", "0.000254"}, 
         {"1.34349E+00", "0.000253"}, 
         {"1.34939E+00", "0.000252"}, 
         {"1.35531E+00", "0.000250"}, 
         {"1.36126E+00", "0.000249"}, 
         {"1.36724E+00", "0.000249"}, 
         {"1.37324E+00", "0.000248"}, 
         {"1.37927E+00", "0.000247"}, 
         {"1.38532E+00", "0.000246"}, 
         {"1.39141E+00", "0.000244"}, 
         {"1.39751E+00", "0.000243"}, 
         {"1.40365E+00", "0.000242"}, 
         {"1.40981E+00", "0.000241"}, 
         {"1.41600E+00", "0.000240"}, 
         {"1.42222E+00", "0.000239"}, 
         {"1.42846E+00", "0.000238"}, 
         {"1.43473E+00", "0.000236"}, 
         {"1.44103E+00", "0.000235"}, 
         {"1.44735E+00", "0.000235"}, 
         {"1.45371E+00", "0.000234"}, 
         {"1.46009E+00", "0.000233"}, 
         {"1.46650E+00", "0.000232"}, 
         {"1.47294E+00", "0.000231"}, 
         {"1.47940E+00", "0.000230"}, 
         {"1.48590E+00", "0.000229"}, 
         {"1.49242E+00", "0.000228"}, 
         {"1.49897E+00", "0.000227"}, 
         {"1.50555E+00", "0.000225"}, 
         {"1.51216E+00", "0.000224"}, 
         {"1.51880E+00", "0.000223"}, 
         {"1.52547E+00", "0.000222"}, 
         {"1.53216E+00", "0.000221"}, 
         {"1.53889E+00", "0.000221"}, 
         {"1.54565E+00", "0.000220"}, 
         {"1.55243E+00", "0.000219"}, 
         {"1.55925E+00", "0.000218"}, 
         {"1.56609E+00", "0.000217"}, 
         {"1.57297E+00", "0.000216"}, 
         {"1.57987E+00", "0.000215"}, 
         {"1.58681E+00", "0.000214"}, 
         {"1.59377E+00", "0.000213"}, 
         {"1.60077E+00", "0.000212"}, 
         {"1.60780E+00", "0.000211"}, 
         {"1.61485E+00", "0.000210"}, 
         {"1.62194E+00", "0.000209"}, 
         {"1.62906E+00", "0.000208"}, 
         {"1.63621E+00", "0.000208"}, 
         {"1.64340E+00", "0.000207"}, 
         {"1.65061E+00", "0.000206"}, 
         {"1.65786E+00", "0.000205"}, 
         {"1.66514E+00", "0.000204"}, 
         {"1.67245E+00", "0.000203"}, 
         {"1.67979E+00", "0.000202"}, 
         {"1.68716E+00", "0.000201"}, 
         {"1.69457E+00", "0.000200"}, 
         {"1.70201E+00", "0.000200"}, 
         {"1.70948E+00", "0.000199"}, 
         {"1.71698E+00", "0.000198"}, 
         {"1.72452E+00", "0.000197"}, 
         {"1.73209E+00", "0.000196"}, 
         {"1.73969E+00", "0.000195"}, 
         {"1.74733E+00", "0.000194"}, 
         {"1.75500E+00", "0.000194"}, 
         {"1.76271E+00", "0.000193"}, 
         {"1.77045E+00", "0.000192"}, 
         {"1.77822E+00", "0.000191"}, 
         {"1.78602E+00", "0.000190"}, 
         {"1.79386E+00", "0.000190"}, 
         {"1.80174E+00", "0.000189"}, 
         {"1.80965E+00", "0.000188"}, 
         {"1.81759E+00", "0.000187"}, 
         {"1.82557E+00", "0.000186"}, 
         {"1.83359E+00", "0.000185"}, 
         {"1.84163E+00", "0.000184"}, 
         {"1.84972E+00", "0.000183"}, 
         {"1.85784E+00", "0.000183"}, 
         {"1.86600E+00", "0.000182"}, 
         {"1.87419E+00", "0.000181"}, 
         {"1.88241E+00", "0.000180"}, 
         {"1.89068E+00", "0.000180"}, 
         {"1.89898E+00", "0.000179"}, 
         {"1.90731E+00", "0.000178"}, 
         {"1.91569E+00", "0.000178"}, 
         {"1.92410E+00", "0.000177"}, 
         {"1.93254E+00", "0.000176"}, 
         {"1.94103E+00", "0.000175"}, 
         {"1.94955E+00", "0.000174"}, 
         {"1.95811E+00", "0.000173"}, 
         {"1.96670E+00", "0.000173"}, 
         {"1.97534E+00", "0.000172"}, 
         {"1.98401E+00", "0.000171"}, 
         {"1.99272E+00", "0.000170"}, 
         {"2.00146E+00", "0.000169"}});

    return model;
  }

  public static Model run7(Model model) {
    model.component("comp1").material("mat7").propertyGroup("RefractiveIndex").func("int2")
         .set("fununit", new String[]{"1"});
    model.component("comp1").material("mat7").propertyGroup("RefractiveIndex").func("int2")
         .set("argunit", new String[]{"um"});
    model.component("comp1").material("mat7").propertyGroup("RefractiveIndex")
         .set("n", new String[]{"nr(c_const/freq)", "0", "0", "0", "nr(c_const/freq)", "0", "0", "0", "nr(c_const/freq)"});
    model.component("comp1").material("mat7").propertyGroup("RefractiveIndex")
         .set("ki", new String[]{"ni(c_const/freq)", "0", "0", "0", "ni(c_const/freq)", "0", "0", "0", "ni(c_const/freq)"});
    model.component("comp1").material("mat7").propertyGroup("RefractiveIndex").addInput("frequency");
    model.component("comp1").material("mat8").label("ZnS [tested at -180 \u00b0C (93.2 K)]");
    model.component("comp1").material("mat8")
         .set("info", new String[][]{{"Composition", "Composition", "67.1 Zn, 32.9 S (wt %) or 50 Zn, 50 S (at %), MW = 97.44 g/mol"}});
    model.component("comp1").material("mat8").propertyGroup("RefractiveIndex").func("RIWL_tested_at_180C_1")
         .label("Piecewise");
    model.component("comp1").material("mat8").propertyGroup("RefractiveIndex").func("RIWL_tested_at_180C_1")
         .set("arg", "WL");
    model.component("comp1").material("mat8").propertyGroup("RefractiveIndex").func("RIWL_tested_at_180C_1")
         .set("pieces", new String[][]{{"500.0", "850.0", "4.267324473-0.00899981259*WL^1+1.622337121E-5*WL^2-1.348232323E-8*WL^3+4.287878788E-12*WL^4"}, 
         {"850.0", "1500.0", "2.856060976-0.001705858938*WL^1+2.054758707E-6*WL^2-1.258893597E-9*WL^3+3.835806818E-13*WL^4-4.602217582E-17*WL^5"}, 
         {"1500.0", "5500.0", "2.33644406-9.323128672E-5*WL^1+4.28784382E-8*WL^2-1.027188811E-11*WL^3+1.211655011E-15*WL^4-5.661538459E-20*WL^5"}, 
         {"5500.0", "14000.0", "2.25611573-1.036958254E-6*WL^1-5.013210924E-10*WL^2+7.633067796E-15*WL^3-1.043005095E-18*WL^4"}});
    model.component("comp1").material("mat8").propertyGroup("RefractiveIndex")
         .set("n", new String[]{"RIWL_tested_at_180C_1(c_const/freq/1[nm])", "0", "0", "0", "RIWL_tested_at_180C_1(c_const/freq/1[nm])", "0", "0", "0", "RIWL_tested_at_180C_1(c_const/freq/1[nm])"});
    model.component("comp1").material("mat8").propertyGroup("RefractiveIndex")
         .setPropertyInfo("n", "Reference: H.H. Li, \"Refractive Index of ZnS, ZnSe, and ZnTe and Its Wavelength and Temperature Derivatives\", Journal of Physical and Chemical Reference Data, v13, No. 1, p103 (1984) https://srd.nist.gov/jpcrdreprint/1.555705.pdf\nNote: tested at -180 \u00b0C (93.2 K), T\u0098\u009a = 1827 \u00b0C (2100 K), CVD films, measured in unknown ambient\nReference temperature: 93.15[K]");
    model.component("comp1").material("mat8").propertyGroup("RefractiveIndex").addInput("frequency");
    model.component("comp1").material("mat9").label("ZnS [tested at -30 \u00b0C (243 K)]");
    model.component("comp1").material("mat9")
         .set("info", new String[][]{{"Composition", "Composition", "67.1 Zn, 32.9 S (wt %) or 50 Zn, 50 S (at %), MW = 97.44 g/mol"}});
    model.component("comp1").material("mat9").propertyGroup("RefractiveIndex").func("RIWL_tested_at_30C_4")
         .label("Piecewise");
    model.component("comp1").material("mat9").propertyGroup("RefractiveIndex").func("RIWL_tested_at_30C_4")
         .set("arg", "WL");
    model.component("comp1").material("mat9").propertyGroup("RefractiveIndex").func("RIWL_tested_at_30C_4")
         .set("pieces", new String[][]{{"500.0", "850.0", "4.444649494-0.009914198593*WL^1+1.810155303E-5*WL^2-1.521969697E-8*WL^3+4.893939394E-12*WL^4"}, 
         {"850.0", "1500.0", "2.857866217-0.001675216989*WL^1+1.995727046E-6*WL^2-1.211084419E-9*WL^3+3.660876505E-13*WL^4-4.364177222E-17*WL^5"}, 
         {"1500.0", "5500.0", "2.341703702-9.182717482E-5*WL^1+4.177750579E-8*WL^2-9.92997668E-12*WL^3+1.164102563E-15*WL^4-5.415384609E-20*WL^5"}, 
         {"5500.0", "14000.0", "2.262429495-1.323776803E-6*WL^1-4.697714121E-10*WL^2+5.848497112E-15*WL^3-9.928331648E-19*WL^4"}});
    model.component("comp1").material("mat9").propertyGroup("RefractiveIndex")
         .set("n", new String[]{"RIWL_tested_at_30C_4(c_const/freq/1[nm])", "0", "0", "0", "RIWL_tested_at_30C_4(c_const/freq/1[nm])", "0", "0", "0", "RIWL_tested_at_30C_4(c_const/freq/1[nm])"});
    model.component("comp1").material("mat9").propertyGroup("RefractiveIndex")
         .setPropertyInfo("n", "Reference: H.H. Li, \"Refractive Index of ZnS, ZnSe, and ZnTe and Its Wavelength and Temperature Derivatives\", Journal of Physical and Chemical Reference Data, v13, No. 1, p103 (1984) https://srd.nist.gov/jpcrdreprint/1.555705.pdf\nNote: tested at -30 \u00b0C (243 K), T\u0098\u009a = 1827 \u00b0C (2100 K), CVD films, measured in unknown ambient\nReference temperature: 243.15[K]");
    model.component("comp1").material("mat9").propertyGroup("RefractiveIndex").addInput("frequency");

    model.component("comp1").cpl("intop1").label("PBS1_in");
    model.component("comp1").cpl("intop2").label("PBS1_top");
    model.component("comp1").cpl("intop3").label("PBS_right");
    model.component("comp1").cpl("intop4").label("PBS_bottom");
    model.component("comp1").cpl("intop5").label("BS1_Bin");
    model.component("comp1").cpl("intop6").label("BS3_Bin");
    model.component("comp1").cpl("intop7").label("Control Qubit");
    model.component("comp1").cpl("intop8").label("Target Qubit");
    model.component("comp1").cpl("intop9").label("BS3_Out");
    model.component("comp1").cpl("aveop1").label("Prism Average");
    model.component("comp1").cpl("aveop2").label("High-Index Average");
    model.component("comp1").cpl("aveop3").label("Low-Index Average");

    model.common("cminpt").set("modified", new String[][]{{"frequency", "c_const/lda0"}});

    model.component("comp1").physics("ewbe").prop("WaveVector").set("PhaseSpec", "UserDefined");
    model.component("comp1").physics("ewbe").prop("WaveVector").set("phase1", "phi1");
    model.component("comp1").physics("ewbe").prop("WaveVector").set("phase2", "phi2");
    model.component("comp1").physics("ewbe").selection()
         .set(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56);
    model.component("comp1").physics("ewbe").feature("webe1").set("minput_frequency_src", "fromCommonDef");
    model.component("comp1").physics("ewbe").feature("mbc1").set("IncidentField", "EField");
    model.component("comp1").physics("ewbe").feature("mbc1")
         .set("E0i", new String[][]{{"0"}, {"E0*exp(-(y/w0)^2)"}, {"E0*exp(-(y/w0)^2)"}});
    model.component("comp1").physics("ewbe").feature("mbc1").set("NoScatteredField", true);
    model.component("comp1").physics("ewbe").feature("mbc1").label("Matched Boundary Condition 1 (Left Source)");
    model.component("comp1").physics("ewbe").feature("mbc2")
         .set("E0i", new String[][]{{"0"}, {"0"}, {"E0*exp(-(y/w0)^2)"}});
    model.component("comp1").physics("ewbe").feature("mbc2").label("Matched Boundary Condition 2 (Vertical)");
    model.component("comp1").physics("ewbe").feature("mbc3").set("InputWave", "SecondWave");
    model.component("comp1").physics("ewbe").feature("mbc3")
         .set("E0i", new String[][]{{"0"}, {"0"}, {"E0*exp(-(y/w0)^2)"}});
    model.component("comp1").physics("ewbe").feature("mbc3").label("Matched Boundary Condition 3 (Horizontal)");
    model.component("comp1").physics("ewbe").feature("mbc4").set("InputWave", "SecondWave");
    model.component("comp1").physics("ewbe").feature("mbc4").set("IncidentField", "EField");
    model.component("comp1").physics("ewbe").feature("mbc4")
         .set("E0i", new String[][]{{"E0*exp(-((x-a-air_domain_length)/w0)^2)"}, {"0"}, {"E0*exp(-((x-a-air_domain_length)/w0)^2)"}});
    model.component("comp1").physics("ewbe").feature("mbc4").set("NoScatteredField", true);
    model.component("comp1").physics("ewbe").feature("mbc4").label("Matched Boundary Condition 4 (Bottom Source)");
    model.component("comp1").physics("ewbe").feature("sctr1")
         .label("Scattering Boundary Condition 1 (remove vertical)");
    model.component("comp1").physics("ewbe").feature("trans1").set("PropagationDirection", "NormalDirection");
    model.component("comp1").physics("ewbe").feature("trans1").set("n_mat", "userdef");
    model.component("comp1").physics("ewbe").feature("trans1").set("n", "n_antireflection");
    model.component("comp1").physics("ewbe").feature("trans1").set("ki_mat", "userdef");
    model.component("comp1").physics("ewbe").feature("trans1").set("d", "d_antireflection");
    model.component("comp1").physics("ewbe").feature("trans1").label("ANTIREFLECTION Transition Boundary Condition");
    model.component("comp1").physics("ewbe").feature("trans2").set("n_mat", "userdef");
    model.component("comp1").physics("ewbe").feature("trans2").set("n", "real(n_Ag)");
    model.component("comp1").physics("ewbe").feature("trans2").set("ki_mat", "userdef");
    model.component("comp1").physics("ewbe").feature("trans2").set("ki", "imag(n_Ag)");
    model.component("comp1").physics("ewbe").feature("trans2").set("epsilonr_mat", "userdef");
    model.component("comp1").physics("ewbe").feature("trans2").set("epsilonr", "eps_Ag");
    model.component("comp1").physics("ewbe").feature("trans2").set("d", "silver_film_thickness");
    model.component("comp1").physics("ewbe").feature("trans2").set("murbnd_mat", "userdef");
    model.component("comp1").physics("ewbe").feature("trans2").set("sigmabnd_mat", "userdef");
    model.component("comp1").physics("ewbe").feature("trans2").label("BEAMSPLITTER Transition Boundary Condition 2");
    model.component("comp1").physics("ge").label("Global ODEs and DAEs (phases)");
    model.component("comp1").physics("ge").feature("ge1")
         .set("name", new String[][]{{"phi100"}, 
         {"phi110"}, 
         {"phi120"}, 
         {"phi130"}, 
         {"phi150"}, 
         {"phi160"}, 
         {"phi170"}, 
         {"phi180"}, 
         {"phi280"}, 
         {"phi270"}, 
         {"phi260"}, 
         {"phi250"}, 
         {"phi230"}, 
         {"phi220"}, 
         {"phi210"}, 
         {"phi200"}, 
         {"phiAirx"}, 
         {"phiAiry"}, 
         {"phiPhaseShifterx"}, 
         {"phiPhaseShiftery"}, 
         {"phiAirxShifted"}, 
         {"phiAiryShifted"}, 
         {"phiPBS_2_Prismx"}, 
         {"phiPBS_2_Prismy"}});
    model.component("comp1").physics("ge").feature("ge1")
         .set("equation", new String[][]{{"k1Prismx*x0+phi100-k1Highx*x0-phi110"}, 
         {"k1Highx*x1+phi110-k1Lowx*x1-phi120"}, 
         {"k1Lowx*x2+phi120-k1Highx*x2-phi130"}, 
         {"k1Highx*x3+phi130-k1Lowx*x3-0"}, 
         {"k1Lowx*x4+0-k1Highx*x4-phi150"}, 
         {"k1Highx*x5+phi150-k1Lowx*x5-phi160"}, 
         {"k1Lowx*x6+phi160-k1Highx*x6-phi170"}, 
         {"k1Highx*x7+phi170-k1Prismx*x7-phi180"}, 
         {"k2Prismy*x0+phi280-k2Highy*x0-phi270"}, 
         {"k2Highy*x1+phi270-k2Lowy*x1-phi260"}, 
         {"k2Lowy*x2+phi260-k2Highy*x2-phi250"}, 
         {"k2Highy*x3+phi250-k2Lowy*x3-0"}, 
         {"k2Lowy*x4+0-k2Highy*x4-phi230"}, 
         {"k2Highy*x5+phi230-k2Lowy*x5-phi220"}, 
         {"k2Lowy*x6+phi220-k2Highy*x6-phi210"}, 
         {"k2Highy*x7+phi210-k2Prismy*x7-phi200"}, 
         {"k1Prismx*x8+phi180-k1Airx*x8-phiAirx"}, 
         {"k2Prismy*x8+phi200-k2Airy*x8-phiAiry"}, 
         {"k1Airx*(200[um])+phiAirx-k1Prismx*(200[um])-phiPhaseShifterx"}, 
         {"k2Airy*(0)[um]+phiAiry-k2Prismy*(0)[um]-phiPhaseShiftery"}, 
         {"k1Prismx*(200+10)[um]+phiPhaseShifterx-k1Airx*(200+10)[um]-phiAirxShifted"}, 
         {"k2Prismy*(0)[um]+phiPhaseShiftery-k2Airy*(0)[um]-phiAiryShifted"}, 
         {"-1*k1Airx*(a/2)+phiAirxShifted - (-1*k1Prismx*(a/2) +phiPBS_2_Prismx)"}, 
         {"k2Airy*(400[um])+phiAiryShifted - (1*k2Prismy*(400[um]) +phiPBS_2_Prismy)"}});
    model.component("comp1").physics("ge").feature("ge1")
         .set("description", new String[][]{{"Phase constant, first wave, layer 0"}, 
         {"Phase constant, first wave, layer 1"}, 
         {"Phase constant, first wave, layer 2"}, 
         {"Phase constant, first wave, layer 3"}, 
         {"Phase constant, first wave, layer 5"}, 
         {"Phase constant, first wave, layer 6"}, 
         {"Phase constant, first wave, layer 7"}, 
         {"Phase constant, first wave, layer 8"}, 
         {"Phase constant, second wave, layer 8"}, 
         {"Phase constant, second wave, layer 7"}, 
         {"Phase constant, second wave, layer 6"}, 
         {"Phase constant, second wave, layer 5"}, 
         {"Phase constant, second wave, layer 3"}, 
         {"Phase constant, second wave, layer 2"}, 
         {"Phase constant, second wave, layer 1"}, 
         {"Phase constant, second wave, layer 0"}, 
         {"Should determine phiAirx"}, 
         {"phiAiry? Phase shift for wave 2 in horizontal air domain?"}, 
         {"phase for wave 1 in phase shifter"}, 
         {"phase for wave 2 in phase shifter"}, 
         {"air domain phase after phase shifting"}, 
         {"air domain phase after phase shifting"}});
    model.component("comp1").physics("ge1")
         .label("Global ODEs and DAEs 1 (refractive indices, useless without function)");
    model.component("comp1").physics("ge1").feature("ge1").set("name", new String[][]{{"nP0"}, {"nH0"}, {"nL0"}});
    model.component("comp1").physics("ge1").feature("ge1")
         .set("equation", new String[][]{{"aveop1(ewbe.nxx)-nP0"}, {"aveop2(ewbe.nxx)-nH0"}, {"aveop3(ewbe.nxx)-nL0"}});
    model.component("comp1").physics("ge1").feature("ge1")
         .set("description", new String[][]{{"Refractive index at center wavelength, prisms"}, {"Refractive index at center wavelength, high-index materials"}, {"Refractive index at center wavelength, low-index materials"}});

    model.component("comp1").mesh("mesh1").feature("size").set("custom", "on");
    model.component("comp1").mesh("mesh1").feature("size").set("hmax", "0.25*w0");
    model.component("comp1").mesh("mesh1").feature("size").set("hmin", 2.65E-5);
    model.component("comp1").mesh("mesh1").feature("edg1").set("smoothcontrol", true);
    model.component("comp1").mesh("mesh1").feature("map3").label("Thin film stack 2");
    model.component("comp1").mesh("mesh1").feature("map3").set("smoothcontrol", true);
    model.component("comp1").mesh("mesh1").feature("map3").feature("dis1").set("numelem", 2);
    model.component("comp1").mesh("mesh1").feature("map2").set("smoothcontrol", true);
    model.component("comp1").mesh("mesh1").feature("map2").feature("dis1").label("PML 5 element enforcer");
    model.component("comp1").mesh("mesh1").feature("map2").feature("dis2").active(false);
    model.component("comp1").mesh("mesh1").feature("map2").feature("dis2").set("numelem", 15);
    model.component("comp1").mesh("mesh1").feature("ftri1").set("smoothcontrol", true);
    model.component("comp1").mesh("mesh1").feature("ftri1").feature("size1").set("custom", "on");
    model.component("comp1").mesh("mesh1").feature("ftri1").feature("size1").set("hmax", "0.25*w0/2");
    model.component("comp1").mesh("mesh1").feature("ftri1").feature("size1").set("hmaxactive", true);
    model.component("comp1").mesh("mesh1").feature("ftri1").feature("size1").set("hmin", 2.65E-5);
    model.component("comp1").mesh("mesh1").feature("ftri1").feature("size1").set("hminactive", true);
    model.component("comp1").mesh("mesh1").feature("ftri1").feature("size1").set("hcurveactive", true);
    model.component("comp1").mesh("mesh1").feature("ftri1").feature("size1").set("hnarrowactive", true);
    model.component("comp1").mesh("mesh1").feature("ftri1").feature("size1").set("hgradactive", true);
    model.component("comp1").mesh("mesh1").run();

    model.study().create("std1");
    model.study("std1").create("param", "Parametric");
    model.study("std1").create("stat", "Stationary");
    model.study("std1").create("freq", "Frequency");
    model.study("std1").feature("stat")
         .set("activate", new String[]{"ewbe", "on", "ge", "on", "ge1", "off", "frame:spatial1", "on", "frame:material1", "on"});
    model.study("std1").feature("freq")
         .set("activate", new String[]{"ewbe", "on", "ge", "off", "ge1", "on", "frame:spatial1", "on", "frame:material1", "on"});

    model.batch().create("p1", "Parametric");
    model.batch("p1").create("so1", "Solutionseq");
    model.batch("p1").create("en1", "Evalnumericalseq");
    model.batch("p1").study("std1");

    model.sol().create("sol1");
    model.sol("sol1").attach("std1");
    model.sol("sol1").create("su1", "StoreSolution");
    model.sol("sol2").tag("sol5");
    model.sol().create("sol3");
    model.sol("sol3").study("std1");
    model.sol("sol3").label("Parametric Solutions 1");

    model.result().dataset().create("dset4", "Solution");
    model.result().dataset("dset4").set("solution", "sol5");
    model.result().dataset().remove("dset2");
    model.result().numerical().create("gev1", "EvalGlobal");
    model.result().numerical("gev1").set("data", "dset3");
    model.result().create("pg1", "PlotGroup2D");
    model.result().create("pg2", "PlotGroup1D");
    model.result().create("pg3", "PlotGroup1D");
    model.result("pg1").set("data", "dset3");
    model.result("pg1").selection().geom("geom1", 2);
    model.result("pg1").selection()
         .set(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56);
    model.result("pg1").create("surf1", "Surface");
    model.result("pg2").set("data", "dset3");
    model.result("pg2").create("glob1", "Global");
    model.result("pg2").feature("glob1")
         .set("expr", new String[]{"BS1_attenuation_factor", "BS3_attenuation_factor", "C_o_fraction", "T_o_fraction", "(BS1_bin+BS3_bin+C_o+T_o)/(2*Pin)"});
    model.result("pg3").set("data", "dset4");
    model.result().export().create("img1", "Image");
    model.result().report().create("rpt1", "Report");
    model.result().report("rpt1").create("tp1", "TitlePage");
    model.result().report("rpt1").create("toc1", "TableOfContents");
    model.result().report("rpt1").create("sec1", "Section");
    model.result().report("rpt1").create("sec2", "Section");
    model.result().report("rpt1").create("sec3", "Section");
    model.result().report("rpt1").feature("sec1").create("root1", "Model");
    model.result().report("rpt1").feature("sec1").create("std1", "Study");
    model.result().report("rpt1").feature("sec2").create("sec1", "Section");
    model.result().report("rpt1").feature("sec2").create("sec2", "Section");
    model.result().report("rpt1").feature("sec2").create("sec3", "Section");
    model.result().report("rpt1").feature("sec2").feature("sec1").create("param1", "Parameter");
    model.result().report("rpt1").feature("sec2").feature("sec2").create("geom1", "Geometry");
    model.result().report("rpt1").feature("sec2").feature("sec3").create("pg1", "PlotGroup");
    model.result().report("rpt1").feature("sec3").create("sec1", "Section");
    model.result().report("rpt1").feature("sec3").create("sec2", "Section");
    model.result().report("rpt1").feature("sec3").feature("sec1").create("pg1", "PlotGroup");
    model.result().report("rpt1").feature("sec3").feature("sec2").create("pg1", "PlotGroup");

    model.nodeGroup().create("grp1", "Results");
    model.nodeGroup("grp1").set("type", "table");
    model.nodeGroup("grp1").placeAfter(null);

    model.study("std1").feature("param").set("sweeptype", "filled");
    model.study("std1").feature("param").set("pname", new String[]{"lda0", "T", "air_domain_length", "w0"});
    model.study("std1").feature("param")
         .set("plistarr", new String[]{"range(790,5,805)", "range(285,5,300)", "range(25,25,75)", "range(50*20*wl0, 50*20*wl0, 150*20*wl0)"});
    model.study("std1").feature("param").set("punit", new String[]{"nm", "", "mm", "m"});
    model.study("std1").feature("param").set("keepsol", "last");
    model.study("std1").feature("freq").set("plist", "c_const/lda0");
    model.study("std1").feature("freq").set("usestol", true);
    model.study("std1").feature("freq").set("stol", 1);
    model.study("std1").feature("freq").set("ftplistmethod", "manual");

    model.batch("p1").set("control", "param");
    model.batch("p1").set("sweeptype", "filled");
    model.batch("p1").set("pname", new String[]{"lda0", "T", "air_domain_length", "w0"});
    model.batch("p1")
         .set("plistarr", new String[]{"range(790,5,805)", "range(285,5,300)", "range(25,25,75)", "range(50*20*wl0, 50*20*wl0, 150*20*wl0)"});
    model.batch("p1").set("punit", new String[]{"nm", "", "mm", "m"});
    model.batch("p1").set("err", true);
    model.batch("p1").feature("so1").set("seq", "sol1");
    model.batch("p1").feature("so1").set("psol", "sol3");
    model.batch("p1").feature("so1")
         .set("param", new String[]{"\"lda0\",\"8.05E-7\",\"T\",\"300\",\"air_domain_length\",\"0.075\",\"w0\",\"0.0027\""});
    model.batch("p1").feature("en1").set("seq", "gev1");
    model.batch("p1").feature("en1").set("table", "tbl9");
    model.batch("p1").attach("std1");

    model.sol("sol1").createAutoSequence("std1");
    model.sol("sol1").feature("su1").label("Solution Store 1.1");
    model.sol("sol5").label("Solution Store 1");

    model.study("std1").runNoGen();

    model.result().numerical("gev1").label("Metrics Table");
    model.result().numerical("gev1").set("table", "tbl7");
    model.result().numerical("gev1")
         .set("expr", new String[]{"BS1_attenuation_factor", "BS3_attenuation_factor", "C_o_fraction", "T_o_fraction"});
    model.result().numerical("gev1").set("unit", new String[]{"1", "1", "1", "1"});
    model.result().numerical("gev1").set("descr", new String[]{"", "", "", ""});
    model.result().numerical("gev1").setResult();
    model.result("pg1").label("Electric Field (ewbe)");
    model.result("pg1").set("smooth", "internal");
    model.result("pg1").feature("surf1").label("Electric Field (ewbe)");
    model.result("pg1").feature("surf1").set("resolution", "normal");
    model.result("pg2").set("xlabel", "Wavelength in vacuum (nm)");
    model.result("pg2").set("smooth", "internal");
    model.result("pg2").set("xlabelactive", false);
    model.result("pg2").feature("glob1").set("descr", new String[]{"BS1_att", "BS3_att", "Co", "To", "Pkept"});
    model.result("pg2").feature("glob1").set("xdatasolnumtype", "all");
    model.result("pg2").feature("glob1").set("xdata", "expr");
    model.result("pg2").feature("glob1").set("xdataexpr", "lda0");
    model.result("pg2").feature("glob1").set("xdataunit", "nm");
    model.result("pg2").feature("glob1").set("xdatadescr", "Wavelength in vacuum");
    model.result("pg2").feature("glob1").set("linewidth", "preference");
    model.result().export("img1").set("options2d", false);
    model.result().export("img1").set("size", "current");
    model.result().export("img1").set("unit", "px");
    model.result().export("img1").set("height", "600");
    model.result().export("img1").set("width", "800");
    model.result().export("img1").set("lockratio", "off");
    model.result().export("img1").set("resolution", "96");
    model.result().export("img1").set("antialias", "on");
    model.result().export("img1").set("zoomextents", "off");
    model.result().export("img1").set("fontsize", "9");
    model.result().export("img1").set("colortheme", "globaltheme");
    model.result().export("img1").set("customcolor", new double[]{1, 1, 1});
    model.result().export("img1").set("background", "color");
    model.result().export("img1").set("gltfincludelines", "on");
    model.result().export("img1").set("title1d", "on");
    model.result().export("img1").set("legend1d", "on");
    model.result().export("img1").set("logo1d", "on");
    model.result().export("img1").set("options1d", "on");
    model.result().export("img1").set("title2d", "on");
    model.result().export("img1").set("legend2d", "on");
    model.result().export("img1").set("logo2d", "on");
    model.result().export("img1").set("options2d", "off");
    model.result().export("img1").set("title3d", "on");
    model.result().export("img1").set("legend3d", "on");
    model.result().export("img1").set("logo3d", "on");
    model.result().export("img1").set("options3d", "off");
    model.result().export("img1").set("axisorientation", "on");
    model.result().export("img1").set("grid", "on");
    model.result().export("img1").set("axes1d", "on");
    model.result().export("img1").set("axes2d", "on");
    model.result().export("img1").set("showgrid", "on");
    model.result().export("img1").set("target", "file");
    model.result().export("img1").set("qualitylevel", "92");
    model.result().export("img1").set("qualityactive", "off");
    model.result().export("img1").set("imagetype", "png");
    model.result().export("img1").set("lockview", "off");
    model.result().export("img1").set("highprecisioncolor", "off");
    model.result().report("rpt1").label("Polarizing Beam Splitter");
    model.result().report("rpt1").set("format", "docx");
    model.result().report("rpt1").set("filename", "user:///polarizing_beam_splitter");
    model.result().report("rpt1").set("imagesize", "large");
    model.result().report("rpt1").feature("tp1").set("frontmatterlayout", "headings");
    model.result().report("rpt1").feature("tp1").set("author", "COMSOL");
    model.result().report("rpt1").feature("tp1")
         .set("summary", "A Gaussian beam is incident on a 45-degree thin-film stack embedded in glass material prisms. The thin-film stack is designed from alternating high and low refractive index materials. The wave will be refracted at the Brewster angle at each internal interface. Thus, mainly p-polarized waves (polarization in the plane of incidence) will be transmitted, whereas mainly s-polarized waves (polarization orthogonal to the plane of incidence) will be reflected. Changing the spot radius for the Gaussian beam modifies the polarization discrimination.\n\nThe reflectance and transmittance spectra are calculated for different Gaussian beam spot radii.");
    model.result().report("rpt1").feature("sec1").label("Software Information");
    model.result().report("rpt1").feature("sec1").feature("root1").label("About the Software");
    model.result().report("rpt1").feature("sec1").feature("std1")
         .set("children", new String[][]{{"param", "on"}, {"stat", "off"}, {"freq", "off"}});
    model.result().report("rpt1").feature("sec2").label("Input Data");
    model.result().report("rpt1").feature("sec2").feature("sec1").label("Parameters");
    model.result().report("rpt1").feature("sec2").feature("sec1").feature("param1")
         .set("children", new String[][]{{"wl0", "on"}, 
         {"dWl", "on"}, 
         {"w0", "on"}, 
         {"nPrism_DESIGN", "on"}, 
         {"nHigh_DESIGN", "on"}, 
         {"nLow_DESIGN", "on"}, 
         {"z0", "off"}, 
         {"a", "on"}, 
         {"nLayers", "on"}, 
         {"dHigh", "on"}, 
         {"dLow", "on"}, 
         {"E0", "off"}, 
         {"lda0", "off"}, 
         {"k0", "off"}, 
         {"kParallel", "off"}, 
         {"kHighNormal", "off"}, 
         {"kLowNormal", "off"}, 
         {"dStack", "off"}, 
         {"wlCount", "on"}, 
         {"alphaHigh", "on"}, 
         {"alphaLow", "on"}, 
         {"xCOGu", "on"}, 
         {"yCOGu", "on"}, 
         {"xCOGl", "on"}, 
         {"yCOGl", "on"}, 
         {"currentiter", "on"}});
    model.result().report("rpt1").feature("sec2").feature("sec2").label("Geometry");
    model.result().report("rpt1").feature("sec2").feature("sec2").feature("geom1").set("includestats", false);
    model.result().report("rpt1").feature("sec2").feature("sec2").feature("geom1")
         .set("children", new String[][]{{"r2", "on"}, 
         {"r3", "on"}, 
         {"r4", "on"}, 
         {"r5", "on"}, 
         {"pi1", "on"}, 
         {"pol1", "on"}, 
         {"pol2", "on"}, 
         {"pol3", "off"}, 
         {"uni1", "on"}, 
         {"rot1", "on"}, 
         {"rot2", "on"}, 
         {"rot3", "on"}, 
         {"pi2", "on"}, 
         {"mov1", "on"}, 
         {"mov2", "on"}, 
         {"mov3", "on"}, 
         {"r6", "on"}, 
         {"r7", "on"}, 
         {"r17", "on"}, 
         {"ls1", "on"}, 
         {"ls2", "on"}, 
         {"ls3", "on"}, 
         {"r8", "on"}, 
         {"r9", "on"}, 
         {"r10", "on"}, 
         {"r11", "on"}, 
         {"r20", "on"}, 
         {"r21", "on"}, 
         {"r22", "on"}, 
         {"r28", "on"}, 
         {"r24", "on"}, 
         {"r12", "on"}, 
         {"r13", "on"}, 
         {"r14", "on"}, 
         {"r15", "on"}, 
         {"r16", "on"}, 
         {"rot5", "on"}, 
         {"rot6", "on"}, 
         {"pi3", "on"}, 
         {"mov4", "on"}, 
         {"mov5", "on"}, 
         {"mov6", "on"}, 
         {"rot7", "on"}, 
         {"rot8", "on"}, 
         {"pi4", "on"}, 
         {"mov7", "on"}, 
         {"mov8", "on"}, 
         {"mov9", "on"}, 
         {"copy1", "on"}, 
         {"r23", "on"}, 
         {"r25", "on"}, 
         {"r27", "on"}, 
         {"r26", "on"}, 
         {"ls5", "on"}, 
         {"r29", "on"}, 
         {"fin", "on"}, 
         {"mce1", "on"}});
    model.result().report("rpt1").feature("sec2").feature("sec3").label("Refractive Index Profile");
    model.result().report("rpt1").feature("sec2").feature("sec3").feature("pg1").label("Refractive Index Profile");
    model.result().report("rpt1").feature("sec2").feature("sec3").feature("pg1").set("noderef", "none");
    model.result().report("rpt1").feature("sec3").label("Results");
    model.result().report("rpt1").feature("sec3").feature("sec1").label("Electric Field Norm");
    model.result().report("rpt1").feature("sec3").feature("sec1").feature("pg1").label("Electric Field Norm");
    model.result().report("rpt1").feature("sec3").feature("sec1").feature("pg1").set("noderef", "none");
    model.result().report("rpt1").feature("sec3").feature("sec2").label("Reflectance and Transmittance");
    model.result().report("rpt1").feature("sec3").feature("sec2").feature("pg1")
         .label("Reflectance and Transmittance");
    model.result().report("rpt1").feature("sec3").feature("sec2").feature("pg1").set("noderef", "none");
    model.result().export("img1").set("size", "current");
    model.result().export("img1").set("unit", "px");
    model.result().export("img1").set("height", "600");
    model.result().export("img1").set("width", "800");
    model.result().export("img1").set("lockratio", "off");
    model.result().export("img1").set("resolution", "96");
    model.result().export("img1").set("antialias", "on");
    model.result().export("img1").set("zoomextents", "off");
    model.result().export("img1").set("fontsize", "9");
    model.result().export("img1").set("colortheme", "globaltheme");
    model.result().export("img1").set("customcolor", new double[]{1, 1, 1});
    model.result().export("img1").set("background", "color");
    model.result().export("img1").set("gltfincludelines", "on");
    model.result().export("img1").set("title1d", "on");
    model.result().export("img1").set("legend1d", "on");
    model.result().export("img1").set("logo1d", "on");
    model.result().export("img1").set("options1d", "on");
    model.result().export("img1").set("title2d", "on");
    model.result().export("img1").set("legend2d", "on");
    model.result().export("img1").set("logo2d", "on");
    model.result().export("img1").set("options2d", "off");
    model.result().export("img1").set("title3d", "on");
    model.result().export("img1").set("legend3d", "on");
    model.result().export("img1").set("logo3d", "on");

    return model;
  }

  public static Model run8(Model model) {
    model.result().export("img1").set("options3d", "off");
    model.result().export("img1").set("axisorientation", "on");
    model.result().export("img1").set("grid", "on");
    model.result().export("img1").set("axes1d", "on");
    model.result().export("img1").set("axes2d", "on");
    model.result().export("img1").set("showgrid", "on");
    model.result().export("img1").set("target", "file");
    model.result().export("img1").set("qualitylevel", "92");
    model.result().export("img1").set("qualityactive", "off");
    model.result().export("img1").set("imagetype", "png");
    model.result().export("img1").set("lockview", "off");
    model.result().export("img1").set("highprecisioncolor", "off");
    model.result().export("img1").set("size", "current");
    model.result().export("img1").set("unit", "px");
    model.result().export("img1").set("height", "600");
    model.result().export("img1").set("width", "800");
    model.result().export("img1").set("lockratio", "off");
    model.result().export("img1").set("resolution", "96");
    model.result().export("img1").set("antialias", "on");
    model.result().export("img1").set("zoomextents", "off");
    model.result().export("img1").set("fontsize", "9");
    model.result().export("img1").set("colortheme", "globaltheme");
    model.result().export("img1").set("customcolor", new double[]{1, 1, 1});
    model.result().export("img1").set("background", "color");
    model.result().export("img1").set("gltfincludelines", "on");
    model.result().export("img1").set("title1d", "on");
    model.result().export("img1").set("legend1d", "on");
    model.result().export("img1").set("logo1d", "on");
    model.result().export("img1").set("options1d", "on");
    model.result().export("img1").set("title2d", "on");
    model.result().export("img1").set("legend2d", "on");
    model.result().export("img1").set("logo2d", "on");
    model.result().export("img1").set("options2d", "off");
    model.result().export("img1").set("title3d", "on");
    model.result().export("img1").set("legend3d", "on");
    model.result().export("img1").set("logo3d", "on");
    model.result().export("img1").set("options3d", "off");
    model.result().export("img1").set("axisorientation", "on");
    model.result().export("img1").set("grid", "on");
    model.result().export("img1").set("axes1d", "on");
    model.result().export("img1").set("axes2d", "on");
    model.result().export("img1").set("showgrid", "on");
    model.result().export("img1").set("target", "file");
    model.result().export("img1").set("qualitylevel", "92");
    model.result().export("img1").set("qualityactive", "off");
    model.result().export("img1").set("imagetype", "png");
    model.result().export("img1").set("lockview", "off");
    model.result().export("img1").set("highprecisioncolor", "off");

    model.nodeGroup("grp1").label("Derived Values testing");
    model.nodeGroup("grp1").add("table", "tbl1");
    model.nodeGroup("grp1").add("table", "tbl2");
    model.nodeGroup("grp1").add("table", "tbl5");
    model.nodeGroup("grp1").add("table", "tbl7");

    return model;
  }

  public static void main(String[] args) {
    Model model = run();
    model = run2(model);
    model = run3(model);
    model = run4(model);
    model = run5(model);
    model = run6(model);
    model = run7(model);
    run8(model);
  }

}
