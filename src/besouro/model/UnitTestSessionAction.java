package besouro.model;

import java.io.File;

import org.eclipse.core.resources.IResource;

import jess.Fact;
import jess.JessException;
import jess.Rete;

public class UnitTestSessionAction extends UnitTestAction {

	public UnitTestSessionAction(Clock clock, IResource workspaceFile) {
		super(clock, workspaceFile);
	}

	@Override
	public Fact assertJessFact(int index, Rete engine) throws JessException {
		return null;
	}

	@Override
	public String toString() {
		return getClock() + " TEST SESSION - " + (this.isSuccessful()?"OK":"FAIL") + " " + getResource();
	}
	
}
