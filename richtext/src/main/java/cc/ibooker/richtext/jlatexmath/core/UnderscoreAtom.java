/* UnderscoreAtom.java
 * =========================================================================
 * This file is part of the JLaTeXMath Library - http://forge.scilab.org/jlatexmath
 *
 * Copyright (C) 2010 DENIZET Calixte
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
 * An atom representing an underscore.
 */
public class UnderscoreAtom extends Atom {

	public static SpaceAtom w = new SpaceAtom(TeXConstants.UNIT_EM, 0.7f, 0, 0);
	public static SpaceAtom s = new SpaceAtom(TeXConstants.UNIT_EM, 0.06f, 0, 0);

	public UnderscoreAtom() {
	}

	public Box createBox(TeXEnvironment env) {
		float drt = env.getTeXFont().getDefaultRuleThickness(env.getStyle());
		HorizontalBox hb = new HorizontalBox(s.createBox(env));
		hb.add(new HorizontalRule(drt, w.createBox(env).getWidth(), 0));
		return hb;
	}
}