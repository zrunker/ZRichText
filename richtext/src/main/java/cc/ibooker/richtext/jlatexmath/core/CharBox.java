/* CharBox.java
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

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;

/**
 * A box representing a single character.
 */
public class CharBox extends Box {

	private final CharFont cf;
	private final float size;

	private final char[] arr = new char[1];

	/**
	 * Create a new CharBox that will represent the character defined by the
	 * given Char-object.
	 * 
	 * @param c
	 *            a Char-object containing the character's font information.
	 */
	public CharBox(Char c) {
		cf = c.getCharFont();
		size = c.getMetrics().getSize();
		width = c.getWidth();
		height = c.getHeight();
		depth = c.getDepth();
	}

	/*public static float pxToSp(Context context, float px) {
		float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		return px / scaledDensity;
	}

	public static float pxToDp(Context context, int dp) {
		float density = context.getResources().getDisplayMetrics().density;
		return dp / density;
	}*/

	public void draw(Canvas g2, float x, float y) {
		drawDebug(g2, x, y);
		g2.save();
		g2.translate(x, y);
		Typeface font = FontInfo.getFont(cf.fontId);
		if (size != 1) {
			g2.scale(size, size);
		}
		Paint st = AjLatexMath.getPaint();
		st.setTextSize(TeXFormula.PIXELS_PER_POINT);
		st.setTypeface(font);
		st.setStyle(Style.FILL);
		st.setAntiAlias(true);
		st.setStrokeWidth(0);
		arr[0] = cf.c;
		g2.drawText(arr, 0, 1, 0, 0, st);
		g2.restore();
	}

	public int getLastFontId() {
		return cf.fontId;
	}

	public String toString() {
		return super.toString() + "=" + cf.c;
	}
}
