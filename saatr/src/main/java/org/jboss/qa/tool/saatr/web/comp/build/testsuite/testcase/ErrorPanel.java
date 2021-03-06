
package org.jboss.qa.tool.saatr.web.comp.build.testsuite.testcase;

import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.jboss.qa.tool.saatr.domain.build.TestCase.Fragment;
import org.jboss.qa.tool.saatr.repo.build.BuildRepository;
import org.jboss.qa.tool.saatr.web.comp.HideableLabel;

/**
 * 
 * @author dsimko@redhat.com
 *
 */
@SuppressWarnings("serial")
class ErrorPanel extends GenericPanel<Fragment> {

    @SpringBean
    private BuildRepository buildRepository; 
    
    @Override
    protected void onConfigure() {
        super.onConfigure();
        setVisible(getModelObject() != null);
    }

    public ErrorPanel(String id) {
        super(id);
        add(new HideableLabel("error.value"));
        add(new HideableLabel("error.message"));
        add(new HideableLabel("error.type"));
    }
}