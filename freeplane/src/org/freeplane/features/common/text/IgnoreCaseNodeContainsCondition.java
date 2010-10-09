/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2008 Dimitry Polivaev
 *
 *  This file author is Dimitry Polivaev
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.freeplane.features.common.text;

import org.freeplane.core.util.TextUtils;
import org.freeplane.features.common.filter.condition.ConditionFactory;
import org.freeplane.features.common.filter.condition.ISelectableCondition;
import org.freeplane.features.common.filter.condition.NodeCondition;
import org.freeplane.features.common.map.NodeModel;
import org.freeplane.n3.nanoxml.XMLElement;

public class IgnoreCaseNodeContainsCondition extends NodeCondition {
	static final String NAME = "ignore_case_node_contains_condition";
	static final String VALUE = "VALUE";

	static ISelectableCondition load(final XMLElement element) {
		return new IgnoreCaseNodeContainsCondition(
			element.getAttribute(NodeTextCompareCondition.ITEM, NodeTextConditionController.FILTER_NODE), 
			element.getAttribute(IgnoreCaseNodeContainsCondition.VALUE, null)
);
	}

	final private String value;
	final private String nodeItem;

	IgnoreCaseNodeContainsCondition(String nodeItem, final String value) {
		super();
		this.value = value.toLowerCase();
		this.nodeItem = nodeItem;
	}

	public boolean checkNode(final NodeModel node) {
		final String text = NodeTextConditionController.getItemForComparison(nodeItem, node);
		return text != null && checkText(text);
	}

	private boolean checkText(final String plainTextContent) {
		return plainTextContent.toLowerCase().indexOf(value) > -1;
	}

	@Override
	protected String createDesctiption() {
		final String nodeCondition = TextUtils.getText(nodeItem);
		final String simpleCondition = TextUtils.getText(ConditionFactory.FILTER_CONTAINS);
		return ConditionFactory.createDescription(nodeCondition, simpleCondition, value, true);
	}

	public void toXml(final XMLElement element) {
		final XMLElement child = new XMLElement();
		child.setName(IgnoreCaseNodeContainsCondition.NAME);
		super.attributesToXml(child);
		child.setAttribute(IgnoreCaseNodeContainsCondition.VALUE, value);
		child.setAttribute(NodeTextCompareCondition.ITEM, nodeItem.toString());
		element.addChild(child);
	}
}
