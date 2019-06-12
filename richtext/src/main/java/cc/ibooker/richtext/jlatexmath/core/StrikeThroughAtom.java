/* SmashedAtom.java
 * =========================================================================
 * This file is part of the JLaTeXMath Library - http://forge.scilab.org/jlatexmath
 *
 * Copyright (C) 2013 DENIZET Calixte
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

/**
 * An atom representing a smashed atom (i.e. with no height and no depth).
 */
public class StrikeThroughAtom extends Atom {

	private Atom at;

	public StrikeThroughAtom(Atom at) {
		this.at = at;
	}

	public Box createBox(TeXEnvironment env) {
		TeXFont tf = env.getTeXFont();
		int style = env.getStyle();
		float axis = tf.getAxisHeight(style);
		float drt = tf.getDefaultRuleThickness(style);
		Box b = at.createBox(env);
		HorizontalRule rule = new HorizontalRule(drt, b.getWidth(),
				-axis + drt, false);
		HorizontalBox hb = new HorizontalBox();
		hb.add(b);
		hb.add(new StrutBox(-b.getWidth(), 0, 0, 0));
		hb.add(rule);

		return hb;
	}
}
