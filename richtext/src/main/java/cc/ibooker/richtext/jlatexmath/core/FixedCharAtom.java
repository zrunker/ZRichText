/* FixedCharAtom.java
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

/**
 * An atom representing a fixed character (not depending on a text style).
 */
public class FixedCharAtom extends CharSymbol {

	private final CharFont cf;

	public FixedCharAtom(CharFont c) {
		cf = c;
	}

	public CharFont getCharFont(TeXFont tf) {
		return cf;
	}

	public Box createBox(TeXEnvironment env) {
		TeXFont tf = env.getTeXFont();
		Char c = tf.getChar(cf, env.getStyle());
		return new CharBox(c);
	}

}
