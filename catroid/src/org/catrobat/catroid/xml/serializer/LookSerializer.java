/**
 *  Catroid: An on-device visual programming system for Android devices
 *  Copyright (C) 2010-2012 The Catrobat Team
 *  (<http://developer.catrobat.org/credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://developer.catrobat.org/license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.xml.serializer;

import java.util.ArrayList;
import java.util.List;

import org.catrobat.catroid.common.LookData;
import org.catrobat.catroid.xml.parser.CatroidXMLConstants;

public class LookSerializer extends Serializer {

	private final String lookTabs = tab + tab + tab;

	@Override
	public List<String> serialize(Object object) throws IllegalArgumentException, IllegalAccessException,
			SecurityException, NoSuchFieldException {
		LookData lookdata = (LookData) object;
		String lookFileName = lookdata.getLookFileName();
		String lookName = lookdata.getLookName();
		List<String> lookStringList = new ArrayList<String>();
		String xmlElementString = "";
		xmlElementString = lookTabs + tab + getStartTag(CatroidXMLConstants.LOOK_DATA_ELEMENT_NAME);
		lookStringList.add(xmlElementString);
		xmlElementString = lookTabs + tab + tab + getElementString(CatroidXMLConstants.FILE_NAME, lookFileName);
		lookStringList.add(xmlElementString);
		xmlElementString = lookTabs + tab + tab + getElementString(CatroidXMLConstants.NAME, lookName);
		lookStringList.add(xmlElementString);
		xmlElementString = lookTabs + tab + getEndTag(CatroidXMLConstants.LOOK_DATA_ELEMENT_NAME);
		lookStringList.add(xmlElementString);

		return lookStringList;
	}

	public List<String> serializeLookList(List<LookData> lookList) throws IllegalArgumentException, SecurityException,
			IllegalAccessException, NoSuchFieldException {
		List<String> lookStrings = new ArrayList<String>();
		lookStrings.add(lookTabs + getStartTag(CatroidXMLConstants.LOOK_LIST_ELEMENT_NAME));
		for (LookData lookData : lookList) {
			lookStrings.addAll(this.serialize(lookData));
		}
		lookStrings.add(lookTabs + getEndTag(CatroidXMLConstants.LOOK_LIST_ELEMENT_NAME));
		return lookStrings;
	}

}
