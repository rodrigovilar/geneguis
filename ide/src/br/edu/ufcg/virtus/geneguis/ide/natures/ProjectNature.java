package br.edu.ufcg.virtus.geneguis.ide.natures;

import org.eclipse.core.runtime.CoreException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;

public class ProjectNature implements IProjectNature {

	public static final String NATURE_ID = "geneguis.projectNature";
	
	@Override
    public void configure() throws CoreException {
        // TODO Auto-generated method stub
    }
 
    @Override
    public void deconfigure() throws CoreException {
        // TODO Auto-generated method stub
    }
 
    @Override
    public IProject getProject() {
        // TODO Auto-generated method stub
        return null;
    }
 
    @Override
    public void setProject(IProject project) {
        // TODO Auto-generated method stub
    }

}
