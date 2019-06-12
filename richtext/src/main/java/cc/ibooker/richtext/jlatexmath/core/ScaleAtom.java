/* ScaleAtom.java
 * =========================================================================
 * This file is part of the JLaTeXMath Library - http://forge.scilab.org/jlatexmath
 *
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

/**
 * An atom representing a scaled Atom.
 */
public class ScaleAtom extends Atom {

	protected Atom base;
	private double xscl, yscl;

	public ScaleAtom(Atom base, double xscl, double yscl) {
		this.type = base.type;
		this.base = base;
		this.xscl = xscl;
		this.yscl = yscl;
	}

	public int getLeftType() {
		return base.getLeftType();
	}

	public int getRightType() {
		return base.getRightType();
	}

	public Box createBox(TeXEnvironment env) {
		return new ScaleBox(base.createBox(env), xscl, yscl);
	}
}
