# Contact Groups Java Project

A desktop contact management application built with Java Swing. Users can create, search, and
sort contacts, and organize them into named groups.

## Features

- Add, update, delete and search contacts (by first or last name prefix)
- Sort contacts by first name, last name, or city
- Create groups, add/remove contacts from a group, and rename or delete groups
- Duplicate group names are rejected to keep group names unique
- Data is persisted locally between runs (Java object serialization)

## Architecture

The app follows a classic MVC split:

```
src/main/java/
├── App/        entry point (App.java)
├── controller/ mediates between the Swing views and the model
├── model/      Contact, Group, ContactManager, GroupManager (plain Java, no Swing dependency)
├── util/       FileManager - loads/saves ContactManager and GroupManager to disk
└── view/       Swing frames (MainFrame, ContactsFrame, GroupsFrame, ...)
```

The `model` package has no dependency on Swing or file I/O, which is what makes it unit-testable
in isolation (see `src/test/java`).

## Requirements

- Java 21+
- Maven 3.9+

## Build & run

```bash
mvn package
java -jar target/contact-groups.jar
```

## Running tests

```bash
mvn test
```

Tests cover the `model` package: input validation, defensive copying, sorting/search, and the
group-uniqueness invariant in `GroupManager`.

## Known limitations

- Persistence uses Java object serialization to flat files (`contacts.dat`, `groups.dat`) in the
  working directory, rather than a database or a portable format like JSON.
- No automated UI tests (Swing views are exercised manually).

## License

MIT — see [LICENSE](LICENSE).
