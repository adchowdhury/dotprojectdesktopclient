package com.zycus.pm.script;

import com.zycus.pm.script.utilities.PMScriptsUtility;
import com.zycus.pm.script.utilities.ResourceUtility;
import com.zycus.pm.script.utilities.RightsGroupUtility;
import com.zycus.pm.script.utilities.RightsUtility;

public class Test {
	public static void main(String[] args) {

		RightsGroupUtility.createRightGroup(1, "add-edit-delete-rights");
		RightsUtility.createRight(1, "add", 1);
		RightsUtility.createRight(2, "edit", 1);
		RightsUtility.createRight(3, "delete", 1);

		// -- Define same structure in PMResourceLookup.java
		ResourceUtility.addResource(1, "ui.common.header", 0);
		ResourceUtility.addResource(2, "ui.eventCreationPage", 0);
		ResourceUtility.addResource(3, "common.draftCollectAnalyseParadigm", 0);
		ResourceUtility.addResource(4, "ui.elp.editColumn", 0);
		ResourceUtility.addResource(5, "ui.elp.respondedCount", 0);
		ResourceUtility.addResource(6, "ui.elp.createEventOption", 0);
		ResourceUtility.addResource(7, "ui.elp.eventNamePopupMenu", 0);
		ResourceUtility.addResource(8, "ui.rfx.header", 0);
		ResourceUtility.addResource(9, "ui.rfx.complexItem", 0);
		ResourceUtility.addResource(10, "ui.report", 0);
		ResourceUtility.addResource(11, "ui.event.multiround", 0);
		ResourceUtility.addResource(12, "module.multiround", 0);
		ResourceUtility.addResource(13, "ui.eventCreationPage.templateDropDown", 0);
		ResourceUtility.addResource(14, "ui.eventCreationPage.saveControls", 0);
		ResourceUtility.addResource(15, "ui.collect.exports.link", 0);

		RightsGroupUtility.associateRightGroupToResource(1, "add-edit-delete-rights", "ui.common.header");

		PMScriptsUtility.assignRight(1, 10161, 1, "ui.eventCreationPage.templateDropDown", "add-edit-delete-rights",
				"edit", true);
	}
}