# Training Infrastructure Module

This module provides database configuration and migration management for the training application.

## Features

- **Multi-DataSource Support**: Configure and switch between different databases using a single property
- **Automatic Migration**: Flyway migrations run automatically on application startup
- **Clear Separation**: Each database has its own configuration, migrations, and entity packages

## Configuration

### Switching Between Data Sources

Set the `application.data-source` property in `application.yml`:

```yaml
application:
  data-source: user  # Options: user, Training-Plan
```

### Available Data Sources

#### 1. User Database (`user`)
- **Purpose**: Stores user-related data (authentication, profiles, etc.)
- **Entity Package**: `com.opentrainer.domain.user`
- **Migration Location**: `db/migration/user`
- **Configuration**: `UserDataSourceConfig.java`

#### 2. Training-Plan Database (`Training-Plan`)
- **Purpose**: Stores training-related data (sessions, plans, exercises, etc.)
- **Entity Package**: `com.opentrainer.domain.training`
- **Migration Location**: `db/migration/Training-Plan`
- **Configuration**: `TrainingPanDataSourceConfig.java`

## Database Configuration

Update the connection properties in `application.yml` for each data source:

```yaml
spring:
  datasource:
    user:
      jdbc-url: jdbc:postgresql://localhost:5432/user_db
      username: user_db_user
      password: user_db_password
```

## Migrations

### Adding New Migrations

1. Navigate to the appropriate migration directory:
   - User DB: `src/main/resources/db/migration/user/`
   - Training-Plan DB: `src/main/resources/db/migration/Training-Plan/`

2. Create a new migration file following Flyway naming convention:
   ```
   V{version}__{description}.sql
   ```

   Examples:
   - `V2__add_user_roles.sql`
   - `V3__create_training_history.sql`

3. Migrations run automatically on application startup

### Migration Best Practices

- Use sequential version numbers (V1, V2, V3, etc.)
- Use descriptive names separated by underscores
- Keep migrations atomic and reversible when possible
- Test migrations on a copy of production data

## Architecture

### Conditional Configuration

The configuration uses Spring's `@ConditionalOnProperty` to activate the correct data source based on the `application.data-source` property.

```java
@ConditionalOnProperty(name = "application.data-source", havingValue = "user")
public class UserDataSourceConfig { ... }
```

### Components

1. **DataSourceProperties**: Reads the `application.data-source` property
2. **DataSourceConfig**: Validates the selected data source
3. **UserDataSourceConfig**: Configures User database beans when selected
4. **TrainingPanDataSourceConfig**: Configures Training-Plan database beans when selected
5. **UserFlywayConfig**: Manages User database migrations
6. **TrainingPanFlywayConfig**: Manages Training-Plan database migrations

## Usage Example

To use the User database:

```yaml
# application.yml
application:
  data-source: user
```

To use the Training-Plan database:

```yaml
# application.yml
application:
  data-source: Training-Plan
```

## Requirements

- Java 17+
- Spring Boot 3.2+
- PostgreSQL (configured databases must exist)
- Flyway 9+

## Common Issues

### Invalid data source error
**Error**: `Invalid data source 'xyz'. Valid values: user, Training-Plan`

**Solution**: Check that `application.data-source` is set to either `user` or `Training-Plan`

### Database connection failed
**Error**: Connection refused or timeout

**Solution**:
1. Verify PostgreSQL is running
2. Check database credentials in `application.yml`
3. Ensure the target database exists
4. Verify network connectivity to database server
