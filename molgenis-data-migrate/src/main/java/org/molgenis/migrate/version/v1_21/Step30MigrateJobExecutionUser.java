package org.molgenis.migrate.version.v1_21;

import static java.util.Objects.requireNonNull;

import javax.sql.DataSource;

import org.molgenis.framework.MolgenisUpgrade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * <ul>
 * <li>Changes JobExecution user columns from foreign key to MolgenisUser to a simple userName.</li>
 * </ul>
 */
public class Step30MigrateJobExecutionUser extends MolgenisUpgrade
{
	private static final Logger LOG = LoggerFactory.getLogger(Step30MigrateJobExecutionUser.class);

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public Step30MigrateJobExecutionUser(DataSource dataSource)
	{
		super(29, 30);
		requireNonNull(dataSource);
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void upgrade()
	{
		LOG.info("Upgrade user attribute in JobExecution entities...");
		updateDataType("JobExecution", "user", "string");
		dropForeignKey("SortaJobExecution");
		dropForeignKey("FileIngestJobExecution");
		dropForeignKey("AnnotationJobExecution");
		dropForeignKey("GavinJobExecution");
		LOG.info("Done.");
	}

	private void updateDataType(String entityFullName, String attributeName, String newDataType)
	{
		LOG.info("Update data type of {}.{} to {}...", entityFullName, attributeName, newDataType);
		String attributeId = jdbcTemplate.queryForObject(
				"SELECT a.identifier FROM entities_attributes ea JOIN attributes a ON ea.attributes = a.identifier WHERE ea.fullName = '"
						+ entityFullName + "' AND a.name='" + attributeName + "'",
				String.class);
		jdbcTemplate.update("UPDATE attributes SET dataType = '" + newDataType
				+ "', refEntity = NULL WHERE identifier = '" + attributeId + "'");
	}

	private void dropForeignKey(String entityFullName)
	{
		LOG.info("Drop foreign key and index from {} to MolgenisUser...", entityFullName);
		jdbcTemplate.update("ALTER TABLE `" + entityFullName + "` DROP FOREIGN KEY `" + entityFullName + "_ibfk_1`");
		jdbcTemplate.update("DROP INDEX user ON `" + entityFullName + "`");
		jdbcTemplate.update("UPDATE `" + entityFullName
				+ "` SET user = (SELECT userName from MolgenisUser WHERE MolgenisUser.ID = `" + entityFullName
				+ "`.user)");
	}
}
