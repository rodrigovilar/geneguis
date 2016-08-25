package br.edu.ufcg.virtus.geneguis.ide;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

public class NewProjectWizard extends Wizard implements INewWizard {

	private static final String WIZARD_NAME = "New Geneguis Project";
	
	
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

		_pageOne = new WizardNewProjectCreationPage("From Scratch Project Wizard");
		_pageOne.setTitle("From Scratch Project");
		_pageOne.setDescription("Create something from scratch.");

		addPage(_pageOne);
	}

}
