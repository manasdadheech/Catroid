/**
 *  Catroid: An on-device graphical programming language for Android devices
 *  Copyright (C) 2010-2011 The Catroid Team
 *  (<http://code.google.com/p/catroid/wiki/Credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://www.catroid.org/catroid_license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *   
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package at.tugraz.ist.catroid.formulaeditor;

import java.util.List;

public class InternToken {

	private String tokenStringValue = "";
	private InternTokenType internTokenType;
	private int internPositionIndex;

	public InternToken(InternTokenType internTokenType) {
		this.internTokenType = internTokenType;
	}

	public InternToken(String tokenStringValue, InternTokenType internTokenType) {
		this.tokenStringValue = tokenStringValue;
		this.internTokenType = internTokenType;
	}

	public void setTokenStringValue(String tokenString) {
		this.tokenStringValue = tokenString;
	}

	public String getTokenSringValue() {
		return this.tokenStringValue;
	}

	public boolean isNumber() {
		if (internTokenType == InternTokenType.NUMBER) {
			return true;
		}

		return false;
	}

	public void appendToTokenStringValue(String stringToAppend) {
		this.tokenStringValue += stringToAppend;
	}

	public void appendToTokenStringValue(List<InternToken> internTokensToAppend) {
		for (InternToken internToken : internTokensToAppend) {
			this.tokenStringValue += internToken.tokenStringValue;
		}

	}

	public InternTokenType getInternTokenType() {
		return this.internTokenType;
	}

	public void setInternTokenType(InternTokenType newInternTokenType) {
		this.internTokenType = newInternTokenType;
	}

	@Override
	public String toString() {
		return internTokenType.getInternTokenPrefix() + tokenStringValue;
	}

	public void setInternPositionIndex(int internPositionIndex) {
		this.internPositionIndex = internPositionIndex;
	}

	public int getInternPositionIndex() {
		return this.internPositionIndex;
	}

	private static InternTokenType getFirstInternTokenType(List<InternToken> internTokens) {
		if (internTokens.size() == 0) {
			return null;
		}

		return internTokens.get(0).getInternTokenType();

	}

	public static boolean isPeriodToken(List<InternToken> internTokens) {
		InternTokenType firstInternTokenType = getFirstInternTokenType(internTokens);

		if (firstInternTokenType == null) {
			return false;
		}

		if (firstInternTokenType == InternTokenType.PERIOD) {
			return true;

		}

		return false;
	}

	public static boolean isFunctionToken(List<InternToken> internTokens) {
		InternTokenType firstInternTokenType = getFirstInternTokenType(internTokens);

		if (firstInternTokenType == null) {
			return false;
		}

		if (firstInternTokenType == InternTokenType.FUNCTION_NAME) {
			return true;

		}

		return false;
	}

	public static boolean isNumberToken(List<InternToken> internTokens) {
		InternTokenType firstInternTokenType = getFirstInternTokenType(internTokens);

		if (firstInternTokenType == null) {
			return false;
		}

		if (firstInternTokenType == InternTokenType.NUMBER) {
			return true;
		}

		return false;
	}

}