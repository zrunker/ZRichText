/* ColorAtom.java
 * =========================================================================
 * This file is originally part of the JMathTeX Library - http://jmathtex.sourceforge.net
 *
 * Copyright (C) 2004-2007 Universiteit Gent
 * Copyright (C) 2009 DENIZET Calixte
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 *
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 *
 */

package cc.ibooker.richtext.jlatexmath.core;

import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * An atom representing the foreground and background color of an other atom.
 */
public class ColorAtom extends Atom implements Row {

	public static Map<String, Integer> Colors = new HashMap<String, Integer>();

	// background color
	private final Integer background;

	// foreground color
	private final Integer color;

	// RowAtom for which the colorsettings apply
	private final RowAtom elements;

	static {
		initColors();
	}

	/**
	 * Creates a new ColorAtom that sets the given colors for the given atom.
	 * Null for a color means: no specific color set for this atom.
	 *
	 * @param atom
	 *            the atom for which the given colors have to be set
	 * @param bg
	 *            the background color
	 * @param i
	 *            the foreground color
	 */
	public ColorAtom(Atom atom, Integer bg, Integer i) {
		elements = new RowAtom(atom);
		background = bg;
		color = i;
	}

	/**
	 * Creates a ColorAtom that overrides the colors of the given ColorAtom if
	 * the given colors are not null. If they're null, the old values are used.
	 *
	 * @param bg
	 *            the background color
	 * @param c
	 *            the foreground color
	 * @param old
	 *            the ColorAtom for which the colorsettings should be overriden
	 *            with the given colors.
	 */
	public ColorAtom(Integer bg, Integer c, ColorAtom old) {
		elements = new RowAtom(old.elements);
		background = (bg == null ? old.background : bg);
		color = (c == null ? old.color : c);
	}

	public Box createBox(TeXEnvironment env) {
		env.isColored = true;
		TeXEnvironment copy = env.copy();
		if (background != null)
			copy.setBackground(background);
		if (color != null)
			copy.setColor(color);
		return elements.createBox(copy);
	}

	public int getLeftType() {
		return elements.getLeftType();
	}

	public int getRightType() {
		return elements.getRightType();
	}

	public void setPreviousAtom(Dummy prev) {
		elements.setPreviousAtom(prev);
	}

	public static int getColor(String s) {
		if (s != null && s.length() != 0) {
			s = s.trim();
			if (s.charAt(0) == '#') {
				return Color.parseColor(s);
			} else if (s.indexOf(',') != -1) {
				StringTokenizer toks = new StringTokenizer(s, ";,");
				int n = toks.countTokens();
				if (n == 3) {
					// RGB model
					try {
						String R = toks.nextToken().trim();
						String G = toks.nextToken().trim();
						String B = toks.nextToken().trim();

						float r = Float.parseFloat(R);
						float g = Float.parseFloat(G);
						float b = Float.parseFloat(B);

						if (r == (int) r && g == (int) g && b == (int) b
								&& R.indexOf('.') == -1 && G.indexOf('.') == -1
								&& B.indexOf('.') == -1) {
							int ir = (int) Math.min(255, Math.max(0, r));
							int ig = (int) Math.min(255, Math.max(0, g));
							int ib = (int) Math.min(255, Math.max(0, b));
							return Color.rgb(ir, ig, ib);
						} else {
							r = (float) Math.min(1, Math.max(0, r));
							g = (float) Math.min(1, Math.max(0, g));
							b = (float) Math.min(1, Math.max(0, b));
							return Color.rgb((int) r, (int) g, (int) b);
						}
					} catch (NumberFormatException e) {
						return Color.BLACK;
					}
				} else if (n == 4) {
					// CMYK model
					try {
						float c = Float.parseFloat(toks.nextToken().trim());
						float m = Float.parseFloat(toks.nextToken().trim());
						float y = Float.parseFloat(toks.nextToken().trim());
						float k = Float.parseFloat(toks.nextToken().trim());

						c = (float) Math.min(1, Math.max(0, c));
						m = (float) Math.min(1, Math.max(0, m));
						y = (float) Math.min(1, Math.max(0, y));
						k = (float) Math.min(1, Math.max(0, k));

						return convColor(c, m, y, k);
					} catch (NumberFormatException e) {
						return Color.BLACK;
					}
				}
			}

			Integer c = Colors.get(s.toLowerCase());
			if (c != null) {
				return c;
			} else {
				if (s.indexOf('.') != -1) {
					try {
						float g = (float) Math.min(1,
								Math.max(Float.parseFloat(s), 0));

						return Color.rgb((int) g, (int) g, (int) g);
					} catch (NumberFormatException e) {
					}
				}

				return Color.parseColor("#" + s);
			}
		}

		return Color.BLACK;
	}

	private static void initColors() {
		Colors.put("black", Color.BLACK);
		Colors.put("white", Color.WHITE);
		Colors.put("red", Color.RED);
		Colors.put("green", Color.GREEN);
		Colors.put("blue", Color.BLUE);
		Colors.put("cyan", Color.CYAN);
		Colors.put("magenta", Color.MAGENTA);
		Colors.put("yellow", Color.YELLOW);
		Colors.put("greenyellow", convColor(0.15f, 0f, 0.69f, 0f));
		Colors.put("goldenrod", convColor(0f, 0.10f, 0.84f, 0f));
		Colors.put("dandelion", convColor(0f, 0.29f, 0.84f, 0f));
		Colors.put("apricot", convColor(0f, 0.32f, 0.52f, 0f));
		Colors.put("peach", convColor(0f, 0.50f, 0.70f, 0f));
		Colors.put("melon", convColor(0f, 0.46f, 0.50f, 0f));
		Colors.put("yelloworange", convColor(0f, 0.42f, 1f, 0f));
		Colors.put("orange", convColor(0f, 0.61f, 0.87f, 0f));
		Colors.put("burntorange", convColor(0f, 0.51f, 1f, 0f));
		Colors.put("bittersweet", convColor(0f, 0.75f, 1f, 0.24f));
		Colors.put("redorange", convColor(0f, 0.77f, 0.87f, 0f));
		Colors.put("mahogany", convColor(0f, 0.85f, 0.87f, 0.35f));
		Colors.put("maroon", convColor(0f, 0.87f, 0.68f, 0.32f));
		Colors.put("brickred", convColor(0f, 0.89f, 0.94f, 0.28f));
		Colors.put("orangered", convColor(0f, 1f, 0.50f, 0f));
		Colors.put("rubinered", convColor(0f, 1f, 0.13f, 0f));
		Colors.put("wildstrawberry", convColor(0f, 0.96f, 0.39f, 0f));
		Colors.put("salmon", convColor(0f, 0.53f, 0.38f, 0f));
		Colors.put("carnationpink", convColor(0f, 0.63f, 0f, 0f));
		Colors.put("magenta", convColor(0f, 1f, 0f, 0f));
		Colors.put("violetred", convColor(0f, 0.81f, 0f, 0f));
		Colors.put("rhodamine", convColor(0f, 0.82f, 0f, 0f));
		Colors.put("mulberry", convColor(0.34f, 0.90f, 0f, 0.02f));
		Colors.put("redviolet", convColor(0.07f, 0.90f, 0f, 0.34f));
		Colors.put("fuchsia", convColor(0.47f, 0.91f, 0f, 0.08f));
		Colors.put("lavender", convColor(0f, 0.48f, 0f, 0f));
		Colors.put("thistle", convColor(0.12f, 0.59f, 0f, 0f));
		Colors.put("orchid", convColor(0.32f, 0.64f, 0f, 0f));
		Colors.put("darkorchid", convColor(0.40f, 0.80f, 0.20f, 0f));
		Colors.put("purple", convColor(0.45f, 0.86f, 0f, 0f));
		Colors.put("plum", convColor(0.50f, 1f, 0f, 0f));
		Colors.put("violet", convColor(0.79f, 0.88f, 0f, 0f));
		Colors.put("royalpurple", convColor(0.75f, 0.90f, 0f, 0f));
		Colors.put("blueviolet", convColor(0.86f, 0.91f, 0f, 0.04f));
		Colors.put("periwinkle", convColor(0.57f, 0.55f, 0f, 0f));
		Colors.put("cadetblue", convColor(0.62f, 0.57f, 0.23f, 0f));
		Colors.put("cornflowerblue", convColor(0.65f, 0.13f, 0f, 0f));
		Colors.put("midnightblue", convColor(0.98f, 0.13f, 0f, 0.43f));
		Colors.put("navyblue", convColor(0.94f, 0.54f, 0f, 0f));
		Colors.put("royalblue", convColor(1f, 0.50f, 0f, 0f));
		Colors.put("cerulean", convColor(0.94f, 0.11f, 0f, 0f));
		Colors.put("processblue", convColor(0.96f, 0f, 0f, 0f));
		Colors.put("skyblue", convColor(0.62f, 0f, 0.12f, 0f));
		Colors.put("turquoise", convColor(0.85f, 0f, 0.20f, 0f));
		Colors.put("tealblue", convColor(0.86f, 0f, 0.34f, 0.02f));
		Colors.put("aquamarine", convColor(0.82f, 0f, 0.30f, 0f));
		Colors.put("bluegreen", convColor(0.85f, 0f, 0.33f, 0f));
		Colors.put("emerald", convColor(1f, 0f, 0.50f, 0f));
		Colors.put("junglegreen", convColor(0.99f, 0f, 0.52f, 0f));
		Colors.put("seagreen", convColor(0.69f, 0f, 0.50f, 0f));
		Colors.put("forestgreen", convColor(0.91f, 0f, 0.88f, 0.12f));
		Colors.put("pinegreen", convColor(0.92f, 0f, 0.59f, 0.25f));
		Colors.put("limegreen", convColor(0.50f, 0f, 1f, 0f));
		Colors.put("yellowgreen", convColor(0.44f, 0f, 0.74f, 0f));
		Colors.put("springgreen", convColor(0.26f, 0f, 0.76f, 0f));
		Colors.put("olivegreen", convColor(0.64f, 0f, 0.95f, 0.40f));
		Colors.put("rawsienna", convColor(0f, 0.72f, 1f, 0.45f));
		Colors.put("sepia", convColor(0f, 0.83f, 1f, 0.70f));
		Colors.put("brown", convColor(0f, 0.81f, 1f, 0.60f));
		Colors.put("tan", convColor(0.14f, 0.42f, 0.56f, 0f));
		Colors.put("gray", convColor(0f, 0f, 0f, 0.50f));
	}

	private static int convColor(final float c, final float m, final float y,
			final float k) {
		final float kk = 1 - k;
		return Color.rgb((int) (255 * kk * (1 - c)),
				(int) (255 * kk * (1 - m)), (int) (255 * kk * (1 - y)));
	}
}
