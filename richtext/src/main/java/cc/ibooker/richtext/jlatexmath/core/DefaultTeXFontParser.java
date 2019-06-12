/* DefaultTeXFontParser.java
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

/* Modified by Calixte Denizet */

package cc.ibooker.richtext.jlatexmath.core;

import android.graphics.Typeface;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Parses the font information from an XML-file.
 */
public class DefaultTeXFontParser {

	/**
	 * if the register font cannot be found, we display an error message but we
	 * do it only once
	 */
	private static boolean registerFontExceptionDisplayed = false;
	private static boolean shouldRegisterFonts = true;
	private static DocumentBuilderFactory factory = DocumentBuilderFactory
			.newInstance();

	private static interface CharChildParser { // NOPMD
		public void parse(Element el, char ch, FontInfo info)
				throws XMLResourceParseException;
	}

	private static class ExtensionParser implements CharChildParser {

		ExtensionParser() {
			// avoid generation of access class
		}

		public void parse(Element el, char ch, FontInfo info)
				throws ResourceParseException {
			int[] extensionChars = new int[4];
			// get required integer attributes
			extensionChars[DefaultTeXFont.REP] = DefaultTeXFontParser
					.getIntAndCheck("rep", el);
			// get optional integer attributes
			extensionChars[DefaultTeXFont.TOP] = DefaultTeXFontParser
					.getOptionalInt("top", el, DefaultTeXFont.NONE);
			extensionChars[DefaultTeXFont.MID] = DefaultTeXFontParser
					.getOptionalInt("mid", el, DefaultTeXFont.NONE);
			extensionChars[DefaultTeXFont.BOT] = DefaultTeXFontParser
					.getOptionalInt("bot", el, DefaultTeXFont.NONE);

			// parsing OK, add extension info
			info.setExtension(ch, extensionChars);
		}
	}

	private static class KernParser implements CharChildParser {

		KernParser() {
			// avoid generation of access class
		}

		public void parse(Element el, char ch, FontInfo info)
				throws ResourceParseException {
			// get required integer attribute
			int code = DefaultTeXFontParser.getIntAndCheck("code", el);
			// get required float attribute
			float kernAmount = DefaultTeXFontParser.getFloatAndCheck("val", el);

			// parsing OK, add kern info
			info.addKern(ch, (char) code, kernAmount);
		}
	}

	private static class LigParser implements CharChildParser {

		LigParser() {
			// avoid generation of access class
		}

		public void parse(Element el, char ch, FontInfo info)
				throws ResourceParseException {
			// get required integer attributes
			int code = DefaultTeXFontParser.getIntAndCheck("code", el);
			int ligCode = DefaultTeXFontParser.getIntAndCheck("ligCode", el);

			// parsing OK, add ligature info
			info.addLigature(ch, (char) code, (char) ligCode);
		}
	}

	private static class NextLargerParser implements CharChildParser {

		NextLargerParser() {
			// avoid generation of access class
		}

		public void parse(Element el, char ch, FontInfo info)
				throws ResourceParseException {
			// get required integer attributes
			String fontId = DefaultTeXFontParser.getAttrValueAndCheckIfNotNull(
					"fontId", el);
			int code = DefaultTeXFontParser.getIntAndCheck("code", el);

			// parsing OK, add "next larger" info
			info.setNextLarger(ch, (char) code, Font_ID.indexOf(fontId));
		}
	}

	public static final String RESOURCE_NAME = "DefaultTeXFont.xml";

	public static final String STYLE_MAPPING_EL = "TextStyleMapping";
	public static final String SYMBOL_MAPPING_EL = "SymbolMapping";
	public static final String GEN_SET_EL = "GeneralSettings";
	public static final String MUFONTID_ATTR = "mufontid";
	public static final String SPACEFONTID_ATTR = "spacefontid";

	protected static ArrayList<String> Font_ID = new ArrayList<String>();
	private static Map<String, Integer> rangeTypeMappings = new HashMap<String, Integer>();
	private static Map<String, CharChildParser> charChildParsers = new HashMap<String, CharChildParser>();

	private Map<String, CharFont[]> parsedTextStyles;

	private Element root;
	private Object base = null;

	static {
		// string-to-constant mappings
		setRangeTypeMappings();
		// parsers for the child elements of a "Char"-element
		setCharChildParsers();
	}

	public DefaultTeXFontParser() throws ResourceParseException, IOException {
		this(AjLatexMath.getAssetManager().open(RESOURCE_NAME), RESOURCE_NAME);
	}

	public DefaultTeXFontParser(InputStream file, String name)
			throws ResourceParseException, IOException {
		factory.setIgnoringElementContentWhitespace(true);
		factory.setIgnoringComments(true);
		try {
			root = factory.newDocumentBuilder().parse(file)
					.getDocumentElement();
		} catch (Exception e) { // JDOMException or IOException
			throw new XMLResourceParseException(name, e);
		}
		file.close();
	}

	public DefaultTeXFontParser(Object base, InputStream file, String name)
			throws ResourceParseException, IOException {
		this.base = base;
		factory.setIgnoringElementContentWhitespace(true);
		factory.setIgnoringComments(true);
		try {
			root = factory.newDocumentBuilder().parse(file)
					.getDocumentElement();
		} catch (Exception e) { // JDOMException or IOException
			throw new XMLResourceParseException(name, e);
		}
		file.close();
	}

	private static void setCharChildParsers() {
		charChildParsers.put("Kern", new KernParser());
		charChildParsers.put("Lig", new LigParser());
		charChildParsers.put("NextLarger", new NextLargerParser());
		charChildParsers.put("Extension", new ExtensionParser());
	}

	public FontInfo[] parseFontDescriptions(FontInfo[] fi, InputStream file,
			String name) throws ResourceParseException {
		if (file == null) {
			return fi;
		}
		ArrayList<FontInfo> res = new ArrayList<FontInfo>(Arrays.asList(fi));
		Element font;
		try {
			font = factory.newDocumentBuilder().parse(file)
					.getDocumentElement();
		} catch (Exception e) {
			throw new XMLResourceParseException("Cannot find the file " + name
					+ "!" + e.toString());
		}

		String fontName = getAttrValueAndCheckIfNotNull("name", font);
		// get required integer attribute
		String fontId = getAttrValueAndCheckIfNotNull("id", font);
		if (Font_ID.indexOf(fontId) < 0)
			Font_ID.add(fontId);
		else
			throw new FontAlreadyLoadedException("Font " + fontId
					+ " is already loaded !");
		// get required real attributes
		float space = getFloatAndCheck("space", font);
		float xHeight = getFloatAndCheck("xHeight", font);
		float quad = getFloatAndCheck("quad", font);

		// get optional integer attribute
		int skewChar = getOptionalInt("skewChar", font, -1);

		// get optional boolean for unicode
		int unicode = getOptionalInt("unicode", font, 0);

		// get different versions of a font
		String bold = null;
		try {
			bold = getAttrValueAndCheckIfNotNull("boldVersion", font);
		} catch (ResourceParseException e) {
		}
		String roman = null;
		try {
			roman = getAttrValueAndCheckIfNotNull("romanVersion", font);
		} catch (ResourceParseException e) {
		}
		String ss = null;
		try {
			ss = getAttrValueAndCheckIfNotNull("ssVersion", font);
		} catch (ResourceParseException e) {
		}
		String tt = null;
		try {
			tt = getAttrValueAndCheckIfNotNull("ttVersion", font);
		} catch (ResourceParseException e) {
		}
		String it = null;
		try {
			it = getAttrValueAndCheckIfNotNull("itVersion", font);
		} catch (ResourceParseException e) {
		}

		// create FontInfo-object
		FontInfo info = new FontInfo(Font_ID.indexOf(fontId), base, fontName,
				unicode, xHeight, space, quad, bold, roman, ss, tt, it);

		if (skewChar != -1) // attribute set
			info.setSkewChar((char) skewChar);

		// process all "Char"-elements
		NodeList listF = font.getElementsByTagName("Char");
		for (int j = 0; j < listF.getLength(); j++)
			processCharElement((Element) listF.item(j), info);

		// parsing OK, add to table
		res.add(info);

		for (int i = 0; i < res.size(); i++) {
			FontInfo fin = res.get(i);
			fin.setBoldId(Font_ID.indexOf(fin.boldVersion));
			fin.setRomanId(Font_ID.indexOf(fin.romanVersion));
			fin.setSsId(Font_ID.indexOf(fin.ssVersion));
			fin.setTtId(Font_ID.indexOf(fin.ttVersion));
			fin.setItId(Font_ID.indexOf(fin.itVersion));
		}

		parsedTextStyles = parseStyleMappings();
		return res.toArray(fi);
	}

	public FontInfo[] parseFontDescriptions(FontInfo[] fi)
			throws ResourceParseException, IOException {
		Element fontDescriptions = (Element) root.getElementsByTagName(
				"FontDescriptions").item(0);
		if (fontDescriptions != null) { // element present
			NodeList list = fontDescriptions.getElementsByTagName("Metrics");
			for (int i = 0; i < list.getLength(); i++) {
				// get required string attribute
				String include = getAttrValueAndCheckIfNotNull("include",
						(Element) list.item(i));
				InputStream is = null;
				if (base == null) {
					fi = parseFontDescriptions(fi, is = AjLatexMath
							.getAssetManager().open(include), include);
				} else {
					fi = parseFontDescriptions(fi, is = AjLatexMath
							.getAssetManager().open(include), include);
				}
				is.close();
			}
		}
		return fi;
	}

	protected void parseExtraPath() throws ResourceParseException, IOException {
		Element syms = (Element) root.getElementsByTagName("TeXSymbols")
				.item(0);
		if (syms != null) { // element present
			// get required string attribute
			String include = getAttrValueAndCheckIfNotNull("include", syms);
			SymbolAtom.addSymbolAtom(
					AjLatexMath.getAssetManager().open(include), include);
		}
		Element settings = (Element) root.getElementsByTagName(
				"FormulaSettings").item(0);
		if (settings != null) { // element present
			// get required string attribute
			String include = getAttrValueAndCheckIfNotNull("include", settings);
			TeXFormula.addSymbolMappings(
					AjLatexMath.getAssetManager().open(include), include);
		}
	}

	private static void processCharElement(Element charElement, FontInfo info)
			throws ResourceParseException {
		// retrieve required integer attribute
		char ch = (char) getIntAndCheck("code", charElement);
		// retrieve optional float attributes
		float[] metrics = new float[4];
		metrics[DefaultTeXFont.WIDTH] = getOptionalFloat("width", charElement,
				0);
		metrics[DefaultTeXFont.HEIGHT] = getOptionalFloat("height",
				charElement, 0);
		metrics[DefaultTeXFont.DEPTH] = getOptionalFloat("depth", charElement,
				0);
		metrics[DefaultTeXFont.IT] = getOptionalFloat("italic", charElement, 0);
		// set metrics
		info.setMetrics(ch, metrics);

		// process children
		NodeList list = charElement.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (node.getNodeType() != Node.TEXT_NODE) {
				Element el = (Element) node;
				Object parser = charChildParsers.get(el.getTagName());
				if (parser == null) // unknown element
					throw new XMLResourceParseException(
							RESOURCE_NAME
									+ ": a <Char>-element has an unknown child element '"
									+ el.getTagName() + "'!");
				else
					// process the child element
					((CharChildParser) parser).parse(el, ch, info);
			}
		}
	}

	public static void registerFonts(boolean b) {
		shouldRegisterFonts = b;
	}

	public static Typeface createFont(String name)
			throws ResourceParseException {
		Typeface f = Typeface.createFromAsset(AjLatexMath.getAssetManager(),
				"fonts/" + name);
		return f;

	}

	public Map<String, CharFont> parseSymbolMappings()
			throws ResourceParseException {
		Map<String, CharFont> res = new HashMap<String, CharFont>();
		Element symbolMappings = (Element) root.getElementsByTagName(
				"SymbolMappings").item(0);
		if (symbolMappings == null)
			// "SymbolMappings" is required!
			throw new XMLResourceParseException(RESOURCE_NAME, "SymbolMappings");
		else { // element present
				// iterate all mappings
			NodeList list = symbolMappings.getElementsByTagName("Mapping");
			for (int i = 0; i < list.getLength(); i++) {
				String include = getAttrValueAndCheckIfNotNull("include",
						(Element) list.item(i));
				Element map;
				try {
					InputStream is = null;
					if (base == null) {
						map = factory
								.newDocumentBuilder()
								.parse(is = AjLatexMath.getAssetManager().open(
										include)).getDocumentElement();
					} else {
						map = factory
								.newDocumentBuilder()
								.parse(is = AjLatexMath.getAssetManager().open(
										include)).getDocumentElement();
					}
					is.close();
				} catch (Exception e) {
					throw new XMLResourceParseException("Cannot find the file "
							+ include + "!");
				}
				NodeList listM = map.getElementsByTagName(SYMBOL_MAPPING_EL);
				for (int j = 0; j < listM.getLength(); j++) {
					Element mapping = (Element) listM.item(j);
					// get string attribute
					String symbolName = getAttrValueAndCheckIfNotNull("name",
							mapping);
					// get integer attributes
					int ch = getIntAndCheck("ch", mapping);
					String fontId = getAttrValueAndCheckIfNotNull("fontId",
							mapping);
					// put mapping in table
					String boldFontId = null;
					try {
						boldFontId = getAttrValueAndCheckIfNotNull("boldId",
								mapping);
					} catch (ResourceParseException e) {
					}

					if (boldFontId == null) {
						res.put(symbolName,
								new CharFont((char) ch, Font_ID.indexOf(fontId)));
					} else {
						res.put(symbolName,
								new CharFont((char) ch,
										Font_ID.indexOf(fontId), Font_ID
												.indexOf(boldFontId)));
					}
				}
			}

			return res;
		}
	}

	public String[] parseDefaultTextStyleMappings()
			throws ResourceParseException {
		String[] res = new String[4];
		Element defaultTextStyleMappings = (Element) root.getElementsByTagName(
				"DefaultTextStyleMapping").item(0);
		if (defaultTextStyleMappings == null)
			return res;
		else { // element present
				// iterate all mappings
			NodeList list = defaultTextStyleMappings
					.getElementsByTagName("MapStyle");
			for (int i = 0; i < list.getLength(); i++) {
				Element mapping = (Element) list.item(i);
				// get range name and check if it's valid
				String code = getAttrValueAndCheckIfNotNull("code", mapping);
				Object codeMapping = rangeTypeMappings.get(code);
				if (codeMapping == null) // unknown range name
					throw new XMLResourceParseException(RESOURCE_NAME,
							"MapStyle", "code",
							"contains an unknown \"range name\" '" + code
									+ "'!");
				// get mapped style and check if it exists
				String textStyleName = getAttrValueAndCheckIfNotNull(
						"textStyle", mapping);
				Object styleMapping = parsedTextStyles.get(textStyleName);
				if (styleMapping == null) // unknown text style
					throw new XMLResourceParseException(RESOURCE_NAME,
							"MapStyle", "textStyle",
							"contains an unknown text style '" + textStyleName
									+ "'!");
				// now check if the range is defined within the mapped text
				// style
				CharFont[] charFonts = parsedTextStyles.get(textStyleName);
				int index = ((Integer) codeMapping).intValue();
				if (charFonts[index] == null) // range not defined
					throw new XMLResourceParseException(RESOURCE_NAME
							+ ": the default text style mapping '"
							+ textStyleName + "' for the range '" + code
							+ "' contains no mapping for that range!");
				else
					// everything OK, put mapping in table
					res[index] = textStyleName;
			}
		}
		return res;
	}

	public Map<String, Float> parseParameters() throws ResourceParseException {
		Map<String, Float> res = new HashMap<String, Float>();
		Element parameters = (Element) root.getElementsByTagName("Parameters")
				.item(0);
		if (parameters == null)
			// "Parameters" is required!
			throw new XMLResourceParseException(RESOURCE_NAME, "Parameters");
		else { // element present
				// iterate all attributes
			NamedNodeMap list = parameters.getAttributes();
			for (int i = 0; i < list.getLength(); i++) {
				String name = ((Attr) list.item(i)).getName();
				// set float value (if valid)
				res.put(name, new Float(getFloatAndCheck(name, parameters)));
			}
			return res;
		}
	}

	public Map<String, Number> parseGeneralSettings()
			throws ResourceParseException {
		Map<String, Number> res = new HashMap<String, Number>();
		// TODO: must this be 'Number' ?
		Element generalSettings = (Element) root.getElementsByTagName(
				"GeneralSettings").item(0);
		if (generalSettings == null)
			// "GeneralSettings" is required!
			throw new XMLResourceParseException(RESOURCE_NAME,
					"GeneralSettings");
		else { // element present
				// set required int values (if valid)
			res.put(MUFONTID_ATTR, Font_ID
					.indexOf(getAttrValueAndCheckIfNotNull(MUFONTID_ATTR,
							generalSettings))); // autoboxing
			res.put(SPACEFONTID_ATTR, Font_ID
					.indexOf(getAttrValueAndCheckIfNotNull(SPACEFONTID_ATTR,
							generalSettings))); // autoboxing
			// set required float values (if valid)
			res.put("scriptfactor",
					getFloatAndCheck("scriptfactor", generalSettings)); // autoboxing
			res.put("scriptscriptfactor",
					getFloatAndCheck("scriptscriptfactor", generalSettings)); // autoboxing

		}
		return res;
	}

	Map<String, CharFont[]> parseTextStyleMappings() {
		return parsedTextStyles;
	}

	private Map<String, CharFont[]> parseStyleMappings()
			throws ResourceParseException {
		Map<String, CharFont[]> res = new HashMap<String, CharFont[]>();
		Element textStyleMappings = (Element) root.getElementsByTagName(
				"TextStyleMappings").item(0);
		if (textStyleMappings == null)
			return res;
		else { // element present
				// iterate all mappings
			NodeList list = textStyleMappings
					.getElementsByTagName(STYLE_MAPPING_EL);
			for (int i = 0; i < list.getLength(); i++) {
				Element mapping = (Element) list.item(i);
				// get required string attribute
				String textStyleName = getAttrValueAndCheckIfNotNull("name",
						mapping);
				String boldFontId = null;
				try {
					boldFontId = getAttrValueAndCheckIfNotNull("bold", mapping);
				} catch (ResourceParseException e) {
				}

				NodeList mapRangeList = mapping
						.getElementsByTagName("MapRange");
				// iterate all mapping ranges
				CharFont[] charFonts = new CharFont[4];
				for (int j = 0; j < mapRangeList.getLength(); j++) {
					Element mapRange = (Element) mapRangeList.item(j);
					// get required integer attributes
					String fontId = getAttrValueAndCheckIfNotNull("fontId",
							mapRange);
					int ch = getIntAndCheck("start", mapRange);
					// get required string attribute and check if it's a known
					// range
					String code = getAttrValueAndCheckIfNotNull("code",
							mapRange);
					Object codeMapping = rangeTypeMappings.get(code);
					if (codeMapping == null)
						throw new XMLResourceParseException(RESOURCE_NAME,
								"MapRange", "code",
								"contains an unknown \"range name\" '" + code
										+ "'!");
					else if (boldFontId == null)
						charFonts[((Integer) codeMapping).intValue()] = new CharFont(
								(char) ch, Font_ID.indexOf(fontId));
					else
						charFonts[((Integer) codeMapping).intValue()] = new CharFont(
								(char) ch, Font_ID.indexOf(fontId),
								Font_ID.indexOf(boldFontId));
				}
				res.put(textStyleName, charFonts);
			}
		}
		return res;
	}

	private static void setRangeTypeMappings() {
		rangeTypeMappings.put("numbers", DefaultTeXFont.NUMBERS); // autoboxing
		rangeTypeMappings.put("capitals", DefaultTeXFont.CAPITALS); // autoboxing
		rangeTypeMappings.put("small", DefaultTeXFont.SMALL); // autoboxing
		rangeTypeMappings.put("unicode", DefaultTeXFont.UNICODE); // autoboxing
	}

	private static String getAttrValueAndCheckIfNotNull(String attrName,
			Element element) throws ResourceParseException {
		String attrValue = element.getAttribute(attrName);
		if (attrValue.equals(""))
			throw new XMLResourceParseException(RESOURCE_NAME,
					element.getTagName(), attrName, null);
		return attrValue;
	}

	public static float getFloatAndCheck(String attrName, Element element)
			throws ResourceParseException {
		String attrValue = getAttrValueAndCheckIfNotNull(attrName, element);

		// try parsing string to float value
		float res = 0;
		try {
			res = (float) Double.parseDouble(attrValue);
		} catch (NumberFormatException e) {
			throw new XMLResourceParseException(RESOURCE_NAME,
					element.getTagName(), attrName,
					"has an invalid real value!");
		}
		// parsing OK
		return res;
	}

	public static int getIntAndCheck(String attrName, Element element)
			throws ResourceParseException {
		String attrValue = getAttrValueAndCheckIfNotNull(attrName, element);

		// try parsing string to integer value
		int res = 0;
		try {
			res = Integer.parseInt(attrValue);
		} catch (NumberFormatException e) {
			throw new XMLResourceParseException(RESOURCE_NAME,
					element.getTagName(), attrName,
					"has an invalid integer value!");
		}
		// parsing OK
		return res;
	}

	public static int getOptionalInt(String attrName, Element element,
			int defaultValue) throws ResourceParseException {
		String attrValue = element.getAttribute(attrName);
		if (attrValue.equals("")) // attribute not present
			return defaultValue;
		else {
			// try parsing string to integer value
			int res = 0;
			try {
				res = Integer.parseInt(attrValue);
			} catch (NumberFormatException e) {
				throw new XMLResourceParseException(RESOURCE_NAME,
						element.getTagName(), attrName,
						"has an invalid integer value!");
			}
			// parsing OK
			return res;
		}
	}

	public static float getOptionalFloat(String attrName, Element element,
			float defaultValue) throws ResourceParseException {
		String attrValue = element.getAttribute(attrName);
		if (attrValue.equals("")) // attribute not present
			return defaultValue;
		else {
			// try parsing string to float value
			float res = 0;
			try {
				res = (float) Double.parseDouble(attrValue);
			} catch (NumberFormatException e) {
				throw new XMLResourceParseException(RESOURCE_NAME,
						element.getTagName(), attrName,
						"has an invalid float value!");
			}
			// parsing OK
			return res;
		}
	}
}
