package org.molgenis.data.support;

import autovalue.shaded.com.google.common.common.collect.Maps;
import org.molgenis.data.Entity;
import org.molgenis.data.MolgenisDataException;
import org.molgenis.data.meta.model.AttributeMetaData;
import org.molgenis.data.meta.model.EntityMetaData;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * Entity decorator that computes computed attributes.
 */
public class EntityWithComputedAttributes implements Entity
{
	private static final long serialVersionUID = 1L;

	private final Entity decoratedEntity;
	private final Map<String, ExpressionEvaluator> expressionEvaluators;

	public EntityWithComputedAttributes(Entity decoratedEntity)
	{
		this.decoratedEntity = requireNonNull(decoratedEntity);
		expressionEvaluators = Maps.newHashMap();
		EntityMetaData emd = decoratedEntity.getEntityMetaData();
		for (AttributeMetaData amd : emd.getAtomicAttributes())
		{
			if (amd.getExpression() != null)
			{
				expressionEvaluators.put(amd.getName(), ExpressionEvaluatorFactory.createExpressionEvaluator(amd, emd));
			}
		}
	}

	@Override
	public Object get(String attributeName)
	{
		ExpressionEvaluator expressionEvaluator = expressionEvaluators.get(attributeName);
		if (expressionEvaluator != null)
		{
			return expressionEvaluator.evaluate(this);
		}
		return decoratedEntity.get(attributeName);
	}

	@Override
	public Iterable<String> getAttributeNames()
	{
		return decoratedEntity.getAttributeNames();
	}

	@Override
	public Boolean getBoolean(String attributeName)
	{
		ExpressionEvaluator expressionEvaluator = expressionEvaluators.get(attributeName);
		if (expressionEvaluator != null)
		{
			return (Boolean) expressionEvaluator.evaluate(this);
		}
		return decoratedEntity.getBoolean(attributeName);
	}

	@Override
	public Date getDate(String attributeName)
	{
		ExpressionEvaluator expressionEvaluator = expressionEvaluators.get(attributeName);
		if (expressionEvaluator != null)
		{
			return (Date) expressionEvaluator.evaluate(this);
		}
		return decoratedEntity.getDate(attributeName);
	}

	@Override
	public Double getDouble(String attributeName)
	{
		ExpressionEvaluator expressionEvaluator = expressionEvaluators.get(attributeName);
		if (expressionEvaluator != null)
		{
			return (Double) expressionEvaluator.evaluate(this);
		}
		return decoratedEntity.getDouble(attributeName);
	}

	@Override
	public Iterable<Entity> getEntities(String attributeName)
	{
		ExpressionEvaluator expressionEvaluator = expressionEvaluators.get(attributeName);
		if (expressionEvaluator != null)
		{
			return (Iterable<Entity>) expressionEvaluator.evaluate(this);
		}
		return decoratedEntity.getEntities(attributeName);
	}

	@Override
	public <E extends Entity> Iterable<E> getEntities(String attributeName, Class<E> clazz)
	{
		ExpressionEvaluator expressionEvaluator = expressionEvaluators.get(attributeName);
		if (expressionEvaluator != null)
		{
			return (Iterable<E>) expressionEvaluator.evaluate(this);
		}
		return decoratedEntity.getEntities(attributeName, clazz);
	}

	@Override
	public Entity getEntity(String attributeName)
	{
		ExpressionEvaluator expressionEvaluator = expressionEvaluators.get(attributeName);
		if (expressionEvaluator != null)
		{
			return (Entity) expressionEvaluator.evaluate(this);
		}
		return decoratedEntity.getEntity(attributeName);
	}

	@Override
	public <E extends Entity> E getEntity(String attributeName, Class<E> clazz)
	{
		ExpressionEvaluator expressionEvaluator = expressionEvaluators.get(attributeName);
		if (expressionEvaluator != null)
		{
			return (E) expressionEvaluator.evaluate(this);
		}
		return decoratedEntity.getEntity(attributeName, clazz);
	}

	@Override
	public EntityMetaData getEntityMetaData()
	{
		return decoratedEntity.getEntityMetaData();
	}

	@Override
	public Object getIdValue()
	{
		return decoratedEntity.getIdValue();
	}

	@Override
	public Integer getInt(String attributeName)
	{
		ExpressionEvaluator expressionEvaluator = expressionEvaluators.get(attributeName);
		if (expressionEvaluator != null)
		{
			return (Integer) expressionEvaluator.evaluate(this);
		}
		return decoratedEntity.getInt(attributeName);
	}

	@Override
	public Object getLabelValue()
	{
		return decoratedEntity.getLabelValue();
	}

	@Override
	public Long getLong(String attributeName)
	{
		ExpressionEvaluator expressionEvaluator = expressionEvaluators.get(attributeName);
		if (expressionEvaluator != null)
		{
			return (Long) expressionEvaluator.evaluate(this);
		}
		return decoratedEntity.getLong(attributeName);
	}

	@Override
	public String getString(String attributeName)
	{
		ExpressionEvaluator expressionEvaluator = expressionEvaluators.get(attributeName);
		if (expressionEvaluator != null)
		{
			return (String) expressionEvaluator.evaluate(this);
		}
		return decoratedEntity.getString(attributeName);
	}

	@Override
	public Timestamp getTimestamp(String attributeName)
	{
		ExpressionEvaluator expressionEvaluator = expressionEvaluators.get(attributeName);
		if (expressionEvaluator != null)
		{
			return (Timestamp) expressionEvaluator.evaluate(this);
		}
		return decoratedEntity.getTimestamp(attributeName);
	}

	@Override
	public java.util.Date getUtilDate(String attributeName)
	{
		ExpressionEvaluator expressionEvaluator = expressionEvaluators.get(attributeName);
		if (expressionEvaluator != null)
		{
			return (java.util.Date) expressionEvaluator.evaluate(this);
		}
		return decoratedEntity.getUtilDate(attributeName);
	}

	@Override
	public void set(String attributeName, Object value)
	{
		if (expressionEvaluators.containsKey(attributeName))
		{
			throw new MolgenisDataException(format("Attribute [%s] is computed", attributeName));
		}
		decoratedEntity.set(attributeName, value);
	}

	@Override
	public void set(Entity values)
	{
		decoratedEntity.set(values);
	}

	@Override
	public void setIdValue(Object id)
	{
		decoratedEntity.setIdValue(id);
	}
}
