/* VerticalBox.java
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

import java.util.ListIterator;

/**
 * A box composed of other boxes, put one above the other.
 */
class VerticalBox extends Box {

    private float leftMostPos = Float.MAX_VALUE;
    private float rightMostPos = Float.MIN_VALUE;

    public VerticalBox() { }

    public VerticalBox(Box b, float rest, int alignment) {
        this();
        add(b);
        if (alignment == TeXConstants.ALIGN_CENTER) {
            StrutBox s = new StrutBox(0, rest / 2, 0, 0);
            super.add(0, s);
            height += rest / 2;
            depth += rest / 2;
            super.add(s);
        } else if (alignment == TeXConstants.ALIGN_TOP) {
            depth += rest;
            super.add(new StrutBox(0, rest, 0, 0));
        } else if (alignment == TeXConstants.ALIGN_BOTTOM) {
            height += rest;
            super.add(0, new StrutBox(0, rest, 0, 0));
        }
    }

    public final void add(Box b) {
        super.add(b);
        if (children.size() == 1) {
            height = b.height;
            depth = b.depth;
        } else
            depth += b.height + b.depth;
        recalculateWidth(b);
    }

    public final void add(Box b, float interline) {
        if (children.size() >= 1) {
            add(new StrutBox(0, interline, 0, 0));
        }
        add(b);
    }

    private void recalculateWidth(Box b) {
        leftMostPos = Math.min(leftMostPos, b.shift);
        rightMostPos = Math.max(rightMostPos, b.shift + (b.width > 0 ? b.width : 0));
        width = rightMostPos - leftMostPos;
    }

    public void add(int pos, Box b) {
        super.add(pos, b);
        if (pos == 0) {
			depth += b.depth + height;
			height = b.height;
        } else
            depth += b.height + b.depth;
        recalculateWidth(b);
    }

    public void draw(Canvas g2, float x, float y) {
        float yPos = y - height;
        for (Box b : children) {
            yPos += b.getHeight();
            b.draw(g2, x + b.getShift() - leftMostPos, yPos);
            yPos += b.getDepth();
        }
    }

    public int getSize() {
        return children.size();
    }

    public int getLastFontId() {
        // iterate from the last child box (the lowest) to the first (the highest)
        // untill a font id is found that's not equal to NO_FONT
        int fontId = TeXFont.NO_FONT;
        for (ListIterator it = children.listIterator(children.size()); fontId == TeXFont.NO_FONT
                 && it.hasPrevious();)
            fontId = ((Box) it.previous()).getLastFontId();

        return fontId;
    }
}
