package besouro.listeners;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.ArrayList;

import junit.framework.Assert;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.junit.Test;

import besouro.listeners.WindowListener;
import besouro.listeners.mock.FakeActionStream;
import besouro.listeners.mock.WindowEventsFactory;
import besouro.model.Action;
import besouro.model.FileOpenedAction;
import besouro.stream.ActionOutputStream;


public class WindowEventsTest {

	
	@Test
	public void shouldGenerateFileOpenEvent() {
		
		final ArrayList<Action> generatedActions = new ArrayList<Action>();
		ActionOutputStream stream = new FakeActionStream(generatedActions);
		
		WindowListener listener = new WindowListener(stream);
		listener.setJavaMeter(WindowEventsFactory.createStubJavaMeter());
		
		String filename = "aFile.java";
		listener.partOpened(WindowEventsFactory.createTestEditor(filename, 12345));
		
		Assert.assertEquals(1, generatedActions.size());
		Assert.assertTrue(generatedActions.get(0) instanceof FileOpenedAction);
		
		FileOpenedAction fileOpenedAction = (FileOpenedAction) generatedActions.get(0);
		Assert.assertEquals(filename, fileOpenedAction.getResource().getName());
		
		Assert.assertEquals(12345, fileOpenedAction.getFileSize());
		
		Assert.assertEquals(11, fileOpenedAction.getMethodsCount());
		Assert.assertEquals(22, fileOpenedAction.getStatementsCount());
		Assert.assertEquals(33, fileOpenedAction.getTestAssertionsCount());
		Assert.assertEquals(44, fileOpenedAction.getTestMethodsCount());
		
	}
	
	@Test
	public void shouldGenerateFileOpenEventAtWindowOpen() {
		
		String filename = "aFile.java";
		
		final ArrayList<Action> generatedActions = new ArrayList<Action>();
		ActionOutputStream stream = new FakeActionStream(generatedActions);
		
		WindowListener listener = new WindowListener(stream);
		
		listener.setJavaMeter(WindowEventsFactory.createStubJavaMeter());
		listener.setWorkbench(WindowEventsFactory.getMockWorkbench(filename));
		
		listener.windowOpened(null);
		
		
		Assert.assertEquals(1, generatedActions.size());
		Assert.assertTrue(generatedActions.get(0) instanceof FileOpenedAction);
		
		FileOpenedAction fileOpenedAction = (FileOpenedAction) generatedActions.get(0);
		Assert.assertEquals(filename, fileOpenedAction.getResource().getName());
		Assert.assertEquals(11, fileOpenedAction.getMethodsCount());
		Assert.assertEquals(22, fileOpenedAction.getStatementsCount());
		Assert.assertEquals(33, fileOpenedAction.getTestAssertionsCount());
		Assert.assertEquals(44, fileOpenedAction.getTestMethodsCount());
		
	}
	
	//TODO [test] more unit tests for window events?
	
	
}
