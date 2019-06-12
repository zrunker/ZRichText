/* GraphicsBox.java
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

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;

/**
 * A box representing a box containing a graphics.
 */
public class GeoGebraLogoBox extends Box {

	private static final Integer gray = Color.rgb(102, 102, 102);
	private static final Integer blue = Color.rgb(153, 153, 255);

	public GeoGebraLogoBox(float w, float h) {
		this.depth = 0;
		this.height = h;
		this.width = w;
		this.shift = 0;
	}

	public void draw(Canvas g2, float x, float y) {
		g2.save();
		Paint st = AjLatexMath.getPaint();
		int c = st.getColor();
		Style s = st.getStyle();
		float w = st.getStrokeWidth();

		g2.translate(x + 0.25f * height / 2.15f, y - 1.75f / 2.15f * height);
		st.setColor(gray);
		st.setStrokeWidth(3.79999995f);
		g2.scale(0.05f * height / 2.15f, 0.05f * height / 2.15f);
		g2.rotate((float) Math.toDegrees((-26 * Math.PI / 180)), 20.5f, 17.5f);
		g2.drawArc(new RectF(0f, 0f, 43f, 32f), 0f, 360f, false, st);
		g2.rotate((float) Math.toDegrees((26 * Math.PI / 180)), 20.5f, 17.5f);
		st.setStyle(Style.STROKE);
		drawCircle(st, g2, 16f, -5f);
		drawCircle(st, g2, -1f, 7f);
		drawCircle(st, g2, 5f, 28f);
		drawCircle(st, g2, 27f, 24f);
		drawCircle(st, g2, 36f, 3f);

		st.setColor(c);
		st.setStyle(s);
		st.setStrokeWidth(w);
		g2.restore();
	}

	private static void drawCircle(Paint st, Canvas g2, float x, float y) {
		st.setColor(blue);
		g2.translate(x, y);
		g2.drawCircle(0, 0, 8, st);
		st.setColor(Color.BLACK);
		st.setStyle(Style.STROKE);
		g2.drawCircle(0, 0, 8, st);
		g2.translate(-x, -y);
	}

	public int getLastFontId() {
		return 0;
	}
}