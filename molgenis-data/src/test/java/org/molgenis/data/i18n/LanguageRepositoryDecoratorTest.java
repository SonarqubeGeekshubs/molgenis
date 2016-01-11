package org.molgenis.data.i18n;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mockito.ArgumentCaptor;
import org.molgenis.data.DataService;
import org.molgenis.data.Entity;
import org.molgenis.data.ManageableRepositoryCollection;
import org.molgenis.data.Repository;
import org.molgenis.data.meta.MetaDataService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LanguageRepositoryDecoratorTest
{
	private Repository decoratedRepo;
	private DataService dataService;
	private LanguageRepositoryDecorator languageRepositoryDecorator;

	@BeforeMethod
	public void setUpBeforeMethod()
	{
		decoratedRepo = mock(Repository.class);
		when(decoratedRepo.getEntityMetaData()).thenReturn(LanguageMetaData.INSTANCE);
		dataService = mock(DataService.class);
		MetaDataService metaDataService = mock(MetaDataService.class);
		ManageableRepositoryCollection defaultBackend = mock(ManageableRepositoryCollection.class);
		when(metaDataService.getDefaultBackend()).thenReturn(defaultBackend);
		when(dataService.getMeta()).thenReturn(metaDataService);
		languageRepositoryDecorator = new LanguageRepositoryDecorator(decoratedRepo, dataService);
	}

	@Test
	public void addStream()
	{
		Entity entity0 = mock(Entity.class);
		when(entity0.getEntityMetaData()).thenReturn(LanguageMetaData.INSTANCE);
		when(entity0.getString(LanguageMetaData.CODE)).thenReturn("nl");

		Entity entity1 = mock(Entity.class);
		when(entity1.getEntityMetaData()).thenReturn(LanguageMetaData.INSTANCE);
		when(entity1.getString(LanguageMetaData.CODE)).thenReturn("de");

		Stream<Entity> entities = Arrays.asList(entity0, entity1).stream();
		assertEquals(languageRepositoryDecorator.add(entities), Integer.valueOf(2));
		verify(decoratedRepo, times(1)).add(entity0);
		verify(decoratedRepo, times(1)).add(entity1);
	}

	@Test
	public void deleteStream()
	{
		Entity entity0 = mock(Entity.class);
		when(entity0.getEntityMetaData()).thenReturn(LanguageMetaData.INSTANCE);
		when(entity0.getString(LanguageMetaData.CODE)).thenReturn("nl");

		Entity entity1 = mock(Entity.class);
		when(entity1.getEntityMetaData()).thenReturn(LanguageMetaData.INSTANCE);
		when(entity1.getString(LanguageMetaData.CODE)).thenReturn("de");

		languageRepositoryDecorator.delete(Stream.of(entity0, entity1));
		verify(decoratedRepo, times(1)).delete(entity0);
		verify(decoratedRepo, times(1)).delete(entity1);
	}

	@SuppressWarnings(
	{ "unchecked", "rawtypes" })
	@Test
	public void updateStream()
	{
		Entity entity0 = mock(Entity.class);
		Stream<Entity> entities = Stream.of(entity0);
		ArgumentCaptor<Stream<Entity>> captor = ArgumentCaptor.forClass((Class) Stream.class);
		doNothing().when(decoratedRepo).update(captor.capture());
		languageRepositoryDecorator.update(entities);
		assertEquals(captor.getValue().collect(Collectors.toList()), Arrays.asList(entity0));
	}

}
