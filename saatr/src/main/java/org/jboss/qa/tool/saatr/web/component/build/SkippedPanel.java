package org.jboss.qa.tool.saatr.web.component.build;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.jboss.qa.tool.saatr.entity.Build.TestsuiteData.TestcaseData.SkippedData;

/**
 * 
 * @author dsimko@redhat.com
 *
 */
@SuppressWarnings("serial")
public class SkippedPanel extends GenericPanel<SkippedData> {

    @Override
    protected void onConfigure() {
        super.onConfigure();
        setVisible(getModelObject() != null);
    }

    public SkippedPanel(String id) {
        super(id);
        add(new Label("skipped.value"));
        add(new Label("skipped.message"));
    }
}