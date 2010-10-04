package besouro.integration;

import junit.framework.Assert;

import org.junit.Test;

import besouro.model.Action;
import besouro.model.Episode;
import besouro.stream.EpisodeFileStorage;
import besouro.stream.FileStorageActionStream;

public class PersistenceTest extends IntegrationTestBaseClass {
	
	@Test
	public void shouldPersistActions() throws Exception {
		
		addTestFirst1Actions();
		
		Action[] actions = FileStorageActionStream.loadFromFile(actionsFile);
		
		// 7 actions in the factory + 2 openFile's and 2 unitTestSession's
		Assert.assertEquals(11, actions.length);
	}
	
	@Test
	public void shouldPersistEpisodes() throws Exception {
		
		addTestFirst1Actions();
		
		Episode[] episodes = EpisodeFileStorage.loadEpisodes(episodesFile);
		
		Assert.assertEquals(1, episodes.length);
	}
	
}
