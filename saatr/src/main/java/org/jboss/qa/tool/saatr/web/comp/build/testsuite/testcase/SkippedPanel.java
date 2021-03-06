
package org.jboss.qa.tool.saatr.web.comp.build.testsuite.testcase;

import org.apache.wicket.markup.html.panel.GenericPanel;
import org.jboss.qa.tool.saatr.domain.build.TestCase.Fragment;
import org.jboss.qa.tool.saatr.web.comp.HideableLabel;

/**
 * 
 * @author dsimko@redhat.com
 *
 */
@SuppressWarnings("serial")
class SkippedPanel extends GenericPanel<Fragment> {

    @Override
    protected void onConfigure() {
        super.onConfigure();
        setVisible(getModelObject() != null);
    }

    public SkippedPanel(String id) {
        super(id);
        add(new HideableLabel("skipped.value"));
        add(new HideableLabel("skipped.message"));
    }
}