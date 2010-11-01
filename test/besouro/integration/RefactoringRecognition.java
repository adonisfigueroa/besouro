package besouro.integration;

import static org.mockito.Mockito.when;
import junit.framework.Assert;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.junit.Test;

import besouro.listeners.mock.JUnitEventFactory;
import besouro.listeners.mock.JavaStructureChangeEventFactory;
import besouro.listeners.mock.ResourceChangeEventFactory;


public class RefactoringRecognition extends IntegrationTestBaseClass {

	@Test 
	public void refactoringCategory1A() throws Exception {
		
		addRefactoring1A_Actions();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("refactoring", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1A", stream.getEpisodes()[0].getSubtype());
	}

	
	@Test 
	public void refactoringCategory1A_2() throws Exception {
		
		// Edit on test
		when(meter.isTest()).thenReturn(true);
		when(meter.getNumOfTestMethods()).thenReturn(1);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",33));
				
		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("TestFile.java", "MyTest", Result.ERROR));
		
		// Edit on test
		when(meter.hasTest()).thenReturn(true);
		when(meter.getNumOfTestMethods()).thenReturn(2);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("TestFile.java",37));
				
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile", Result.OK));
		
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		// two refactorings - one on each edit (because they precede test-pass)
		Assert.assertEquals("refactoring", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1A", stream.getEpisodes()[0].getSubtype());
		
//		Assert.assertEquals("refactoring", stream.getTDDMeasure().getRecognizedEpisodes().get(1).getCategory());
//		Assert.assertEquals("1A", stream.getTDDMeasure().getRecognizedEpisodes().get(1).getSubtype());
	}
	
	@Test 
	public void refactoringCategory1B() throws Exception {
		
		// Add test method
		javaListener.elementChanged(JavaStructureChangeEventFactory.createRemoveMethodAction("TestFile.java", "TestFile", "aTestMethod"));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile", Result.OK));
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("refactoring", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("1B", stream.getEpisodes()[0].getSubtype());
		
		//TODO [rule]  redundancy: refactoring and regression
		//			  does it influence the metric?
//		Assert.assertEquals("refactoring", stream.getTDDMeasure().getRecognizedEpisodes().get(1).getCategory());
//		Assert.assertEquals("3", stream.getTDDMeasure().getRecognizedEpisodes().get(1).getSubtype());
//		
//		Assert.assertEquals("refactoring", stream.getTDDMeasure().getRecognizedEpisodes().get(2).getCategory());
//		Assert.assertEquals("2B", stream.getTDDMeasure().getRecognizedEpisodes().get(2).getSubtype());
//		
//		Assert.assertEquals("regression", stream.getTDDMeasure().getRecognizedEpisodes().get(3).getCategory());
//		Assert.assertEquals("1", stream.getTDDMeasure().getRecognizedEpisodes().get(3).getSubtype());
	}
	
	
	@Test 
	public void refactoringCategory2A() throws Exception {
		
		 // Edit on production code    
		when(meter.hasTest()).thenReturn(false);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",34));
	    
	    // Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("TestFile", "MyTest", Result.ERROR));

	    // Edit on production code
		when(meter.hasTest()).thenReturn(false);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",35));

	    
	    // Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile", Result.OK));
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("refactoring", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("2A", stream.getEpisodes()[0].getSubtype());
	    
	}
	
	@Test 
	public void refactoringCategory2B() throws Exception {
		
		// Add prod method
		javaListener.elementChanged(JavaStructureChangeEventFactory.createRemoveMethodAction("ProductionFile.java", "ProductionFile", "aMethod"));
		
		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("TestFile", "MyTest", Result.ERROR));
		
		// rename prod method
		javaListener.elementChanged(JavaStructureChangeEventFactory.createRenameMethodEvent("ProductionFile.java", "ProductionFile", "aMethod", "anotherMethod"));
		
		// Unit test pass
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("testSessionName", "TestFile", Result.OK));
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("refactoring", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("2B", stream.getEpisodes()[0].getSubtype());

		//TODO [rule]  redundancy: refactoring and regression
		//			  does it influence the metric?
//		Assert.assertEquals("regression", stream.getTDDMeasure().getRecognizedEpisodes().get(1).getCategory());
//		Assert.assertEquals("2", stream.getTDDMeasure().getRecognizedEpisodes().get(1).getSubtype());
//		
//		Assert.assertEquals("refactoring", stream.getTDDMeasure().getRecognizedEpisodes().get(2).getCategory());
//		Assert.assertEquals("2B", stream.getTDDMeasure().getRecognizedEpisodes().get(2).getSubtype());
		
	}

	@Test 
	public void refactoringCategory3_1() throws Exception {
		
		 // Edit on production code    
		when(meter.isTest()).thenReturn(false);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",34));
		
		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("TestFile", "MyTest", Result.ERROR));
		
		addRefactoring1A_Actions();
		
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("refactoring", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("3", stream.getEpisodes()[0].getSubtype());
		
	}
	
	@Test 
	public void refactoringCategory3_2() throws Exception {
		
		// Add prod method
		javaListener.elementChanged(JavaStructureChangeEventFactory.createRemoveMethodAction("ProductionFile.java", "ProductionFile", "aMethod"));
		
		// Edit on production code    
		when(meter.isTest()).thenReturn(false);
		resourceListener.resourceChanged(ResourceChangeEventFactory.createEditAction("ProductionFile.java",34));
		
		// Unit test failue
		junitListener.sessionFinished(JUnitEventFactory.createJunitSession("TestFile", "MyTest", Result.ERROR));
		
		addRefactoring1A_Actions();
		
		//TODO [rule]  redundancy: 2 refactorings
		Assert.assertEquals(1, stream.getEpisodes().length);
		Assert.assertEquals("refactoring", stream.getEpisodes()[0].getCategory());
		Assert.assertEquals("3", stream.getEpisodes()[0].getSubtype());
		
//		Assert.assertEquals("refactoring", stream.getTDDMeasure().getRecognizedEpisodes().get(1).getCategory());
//		Assert.assertEquals("3", stream.getTDDMeasure().getRecognizedEpisodes().get(1).getSubtype());
		
	}
}
