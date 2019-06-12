/* FcscoreAtom.java
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
 * An atom with representing an L with a caron.
 */
public class FcscoreAtom extends Atom {

	private int N;

	public FcscoreAtom(int N) {
		this.N = N;
	}

	public int getLeftType() {
		return TeXConstants.TYPE_ORDINARY;
	}

	public int getRightType() {
		return TeXConstants.TYPE_ORDINARY;
	}

	public Box createBox(TeXEnvironment env) {
		final float factor = 12 * SpaceAtom
				.getFactor(TeXConstants.UNIT_MU, env);

		return new FcscoreBox(N == 5 ? 4 : N, factor * 1f, factor * 0.07f,
				factor * 0.125f, N == 5);
	}
}