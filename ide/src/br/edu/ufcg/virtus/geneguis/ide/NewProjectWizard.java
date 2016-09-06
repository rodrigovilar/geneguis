package br.edu.ufcg.virtus.geneguis.ide;

import java.net.URI;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import br.edu.ufcg.virtus.geneguis.ide.projects.NewProjectSupport;


public class NewProjectWizard extends Wizard implements INewWizard, IExecutableExtension {

	private static final String PAGE_NAME = "From Scratch Project Wizard"; //$NON-NLS-1$

	private static final String WIZARD_NAME = "New Geneguis Project"; //$NON-NLS-1$
	
	
	private WizardNewProjectCreationPage _pageOne;
	private IConfigurationElement _configurationElement;

	public NewProjectWizard() {
		setWindowTitle(WIZARD_NAME);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean performFinish() {
		String name = _pageOne.getProjectName();
	    URI location = null;
	    if (!_pageOne.useDefaults()) {
	        location = _pageOne.getLocationURI();
	    } // else location == null
	 
	    NewProjectSupport.createProject(name, location);
	    
	    BasicNewProjectResourceWizard.updatePerspective(_configurationElement);
	 
	    return true;
	}

	@Override
	public void addPages() {
		super.addPages();

		_pageOne = new WizardNewProjectCreationPage(PAGE_NAME);
		_pageOne.setTitle(Messages.getString("NewProjectWizard.GeneguisNewProjectWizard_Geneguis_Project")); //$NON-NLS-1$
		_pageOne.setDescription(Messages.getString("NewProjectWizard.GeneguisNewProjectWizard_Create_From_Scratch")); //$NON-NLS-1$

		addPage(_pageOne);
	}

	@Override
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {
		_configurationElement = config;		
	}

}
