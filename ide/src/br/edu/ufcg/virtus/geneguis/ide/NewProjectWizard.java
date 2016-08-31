package br.edu.ufcg.virtus.geneguis.ide;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

public class NewProjectWizard extends Wizard implements INewWizard {

	private static final String PAGE_NAME = "From Scratch Project Wizard"; //$NON-NLS-1$


	private static final String WIZARD_NAME = "New Geneguis Project"; //$NON-NLS-1$
	
	
	private WizardNewProjectCreationPage _pageOne;

	public NewProjectWizard() {
		setWindowTitle(WIZARD_NAME);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean performFinish() {
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

}
