---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---

# InsuraConnect Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("policy 1 po/Policy XYZ")` API call as an example.

<puml src="diagrams/PolicySequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `policy 1 po/Policy XYZ` Command" />

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.addressbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Undo/redo feature

#### Implementation

The undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />


<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

The following sequence diagram shows how a redo operation goes through the `Logic` component instead:

<puml src="diagrams/RedoSequenceDiagram-Logic.puml" alt="Interactions Inside the Logic Component for the `redo` Command" />

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />



#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

### Client Status

#### Implementation

The client status feature is facilitated by the `ClientStatus` attribute of each `Person`.
`ClientStatus` uses the `Status` Enumeration to record the status of the `Person`.

The `Status` Enum has 4 values:
* `NOT_CLIENT` &mdash; A `Person` who is a partner can only have this value which cannot be changed.
* `START` &mdash; The minimum value for a client. All new clients will start with this value by default.
* `MIDDLE` &mdash; The middle value for a client.
* `END` &mdash; The maximum value for a client.

`ClientStatus` implements the following relevant methods:
* `initNotClientStatus()` &mdash; Returns a `ClientStatus` that does not have a level. Only to be used for partners.
* `initClientStatus()` &mdash; Returns a `ClientStatus` that has the lowest level. Only to be used for clients.
* `getStatus()` &mdash; Returns the ordinal of the `Status` enum.
* `increment()` &mdash; Returns a new `ClientStatus` with one value higher, up to the maximum value `END`.
* `decrement()` &mdash; Returns a new `ClientStatus` with one value lower, up to the minimum value `START`.
* `canIncrement()` &mdash; Checks if the `ClientStatus` can be incremented.
* `canDecrement()` &mdash; Checks if the `ClientStatus` can be decremented.

The following class diagram shows the `ClientStatus` and `Status` classes in relation with `Person`. Other classes associated with `Person` are omitted for clarity.

<puml src="diagrams/ClientStatusClassDiagram.puml" width="250"/>

Given below is an example usage scenario and how the client status feature behaves at each step.

Step 1: The user executes `add n/David ... r/client` to add a new client. Since the relationship is a `client`, the new
person will be created with a `ClientStatus` that has value `START`.

Step 2: Assuming the person the user just added is the first person, the user executes `status 1 s/up` to increment the status.
The value of `Status` that `ClientStatus` holds will now be incremented to `MIDDLE`.

The following activity diagrams summarise what happens when the user attempts to increment and decrement a person's status.

<puml src="diagrams/IncrementClientStatusActivityDiagram.puml"/>

#### Design considerations:

**Aspect: If `ClientStatus` should exist for partners:**
* **Alternative 1 (current choice):** Partners have a `ClientStatus`, but the value is unique and cannot be changed.
  
  * Pros: Easy to implement, less change required to other parts of the model.
  * Cons: Less accurate to real-life model where partners would not have a client status.
* **Alternative 2:** `ClientStatus` is wrapped in an `Optional` so partners will not have a valid `ClientStatus`.
  
  * Pros: More accurate to real-life model.
  * Cons: More challenging to implement, and may result in more bugs.

**Aspect: How the values of status are stored:**
* **Alternative 1 (current choice):** `Status` Enum within `ClientStatus`
  * Pros: Easy to implement and keep track, more extensible for future use.
  * Cons: Additional layer of abstraction.
* **Alternative 2:** Use integers/strings to store values.
  * Pros: Everything contained in one class.
  * Cons: Less extensible, more prone to errors if inputs are not constrained.

### Relationship

#### Implementation

The relationship feature is facilitated by the `Relationship` attribute of each `Person`. `Relationship` class contains a String field that represents the `relationship` between the user and the person, and it can only be `partner` or `client`.

`Relationship` implements the following relevant methods:
* Relationship(String) &mdash; Constructor for relationship which ensures that whether the input is a mix of uppercase and lowercase, it will result in lowercase form. Example: input `ParTNer` will be turned into `partner`.
* isValidRelationship(String) &mdash; Checks if the given relationship is `client` or `partner`.

The following class diagram shows the `Relationship` classes in relation to `Person`. Other classes associated with `Person` are omitted for clarity.

<puml src="diagrams/PolicyClassDiagram.puml" width="250"/>

#### Design considerations:

**Aspect: Extensibility and Future Compatibility:**
* **Alternative 1 (current choice):** Restrict the relationship options to only "partner" or "client" using a String field with validation checks.
  * Pros: Simplified implementation, easy to understand and maintain.
  * Cons: Limited flexibility for adding new relationship types in the future, may require changes to the data model if new relationship types are introduced.
* **Alternative 2:** Implement a more flexible relationship system using a predefined set of relationship types stored in an enum or database table. This allows for easier addition of new relationship types without modifying the codebase.
  * Pros: Improved extensibility, easier to accommodate future changes or additions to relationship types.
  * Cons: Increased complexity in implementation, potential overhead in managing a larger set of relationship types.
* **Alternative 3:** Introduce a relationship hierarchy where relationships can have parent-child relationships. For example, "partner" could be a parent relationship with a child relationship such as "spouse" or "domestic partner".
  * Pros: More granular control over relationship types, and supports complex relationship structures.
  * Cons: Increased complexity in implementation and management, the potential for confusion in understanding relationship hierarchies.

### Find feature

#### Implementation

The find feature is facilitated by `NameContainsKeywordPredicate`, `PolicyContainsKeywordPredicate`, `RelationshipContainsKeywordPredicate` and `TagContainsKeywordPredicate`. It extends `Predicate` which searches through all the contact lists with the given keyword provided with respect to the prefix.

All four classes implement the following relevant methods:
* `NameContainsKeywordPredicate#test(Person)` &mdash; Checks if any of the names associated with a person contain any of the specified keywords.
* `PolicyContainsKeywordPredicate#test(Person)` &mdash; Checks if any of the policies associated with a person contain any of the specified keywords.
* `RelationshipContainsKeywordPredicate#test(Person)` &mdash; Checks if any of the relationships associated with a person contain any of the specified keywords.
* `TagContainsKeywordPredicate#test(Person)` &mdash; Checks if any of the tags associated with a person contain any of the specified keywords.

Given below is an example usage scenario and how the find feature behaves at each step.

Step 1: The user executes `find n/Alice n/Bob` to find those persons containing these names in the contact list. The contact list will now list out all the people with names contain `Alice` or `Bob`. 

Step 2: The user then decides to find his client, therefore he decides to find based on `relationship` by executing the command `find r/client`. Now, the contact list will list all the clients.

Step 3: Instead of finding persons based on one attribute, the user decides to find persons with multiple attributes. He executes the command `find n/Alice r/client t/friend`. Any of the persons in the contact list that fulfill one of these keywords based on the prefix will be listed out. 

The following sequence diagram shows how the find command goes through the Logic component:

<puml src="diagrams/FindSequenceDiagram.puml"/>

#### Design considerations:

**Aspect: Search Precision and Customization:**
* **Alternative 1 (current choice):** Implement separate predicates for different search criteria (name, policy, relationship, tag), allowing users to customize their search based on specific attributes.
  * Pros: Provides precise control over search criteria, and enables users to target specific attributes for more focused searches.
  * Cons: Increased complexity with multiple classes, the potential for confusion if users are not familiar with the available search options.
* **Alternative 2:** Use a single, generic predicate for searching all attributes simultaneously, without differentiation between attributes.
  * Pros: Simplified implementation, easier for users to perform general searches without specifying attribute types.
  * Cons: Less precise search results, may return irrelevant matches if keywords are found in multiple attributes.
* **Alternative 3:** Allow users to specify custom search criteria by combining different predicates dynamically. For example, users could specify a combination of name and relationship predicates to find clients with specific names.
  * Pros: Maximizes flexibility and customization options for users, and allows for complex search scenarios.
  * Cons: Increased complexity in usage, the potential for user errors in specifying custom search criteria.

### Policy

#### Implementation

The policy feature is facilitated by the `Policy` attribute of each `Person`. `Policy` class keeps track of three values which are `value`, `expiryDate` and `premium`. `Policy` consists of two constructors which enable `expiryDate` and `premium` to be optional. The default values of expiryDate and premium are null and 0 respectively. 
* `value` &mdash; A `String` type that contains the name of the policy.
* `expiryDate` &mdash; A `LocalDate` type that records the expiryDate of the policy.
* `premium` &mdash; A `Double` type that holds the premium value of the policy.

`Policy` implements the following relevant methods:
* `Policy(String)` &mdash; Constructor for policy without expiryDate and premium, both values will be set as default.
* `Policy(String, LocalDate, double)` &mdash; Constructor for policy with expiryDate and premium value.
* `isValidExpiryDate(LocalDate)` &mdash; Checks if the given expiryDate is a valid expiry date.
* `isValidPremium(double)` &mdash; Checks if the given premium is a valid premium.
  
The following class diagram shows the `Policy` classes in relation to `Person`. Other classes associated with `Person` are omitted for clarity. Only `client` relationships can hold policies and have a series of actions to them.

<puml src="diagrams/PolicyClassDiagram.puml" width="250"/>

Given below is an example usage scenario and how the policy feature behaves at each step.

Step 1: The user executes `add n/David ... r/client` to add a new policy. Since the relationship is a `client`, the person is allowed to add policies. The new person will have an empty policy in default.

Step 2: Assuming the person the user just added is the first person, the user executes `policy 1 po/Policy ABC` to add the policy. The person now holds one `policy` which is named Policy XYZ and doesn't contain any expiryDate and premium.

**Note:** The action (add, edit or delete) depends on the user input. Add action input doesn't need to be accompanied by a policy index, the other two values are optional. Edit action input needs to contain policy index and policy value, the other two values are optional. Delete action input needs to contain the policy index and leave blank for policy value.

The following sequence diagrams show how the `policy` command goes through the `Logic` component:

<puml src="diagrams/PolicySequenceDiagram.puml"/>

The following activity diagrams summarise what happens when the user attempts to add, edit or delete a person's policy

<puml src="diagrams/PolicyActivityDiagram.puml"/>

#### Design considerations:

**Aspect: Persistence of Policy Data:**
* **Alternative 1 (current choice):** Store policy data directly within the Person class.
  * Pros: Simplified data structure, easier to access policy information.
  * Cons: Tight coupling between Person and Policy, potential scalability issues with large datasets.
* **Alternative 2:** Implement a separate database table for policies linked to persons.
  * Pros: Better separation of concerns, and improved scalability.
  * Cons: Increased complexity in database queries and maintenance, potential performance overhead.

**Aspect: Policy Validation and Enforcement:**
* **Alternative 1 (current choice):** Implement validation checks within the Policy class to ensure that expiry dates are in the future and premium values are non-negative.
  * Pros: Centralized validation logic, easier to maintain and update.
  * Cons: Limited flexibility for custom validation rules, the potential for increased complexity as validation rules evolve.
* **Alternative 2:** Implement a separate policy validation service that encapsulates validation logic for policies. This service can be injected into the Policy class or used externally to validate policies before they are added or updated.
  * Pros: Separation of concerns, allows for more flexible validation rules and customization.
  * Cons: Increased complexity due to the need for additional service integration and potential overhead.
* **Alternative 3:** Use a validation framework or library (e.g., Java Bean Validation) to annotate policy attributes with validation constraints. These annotations can enforce validation rules automatically based on predefined constraints.
  * Pros: Standardized approach to validation, reduces boilerplate code, supports complex validation rules.
  * Cons: Dependency on external libraries, less control over validation logic compared to custom implementations.



### Meeting

#### Implementation

The meeting feature is supported by the `Meeting` class, which is associated with the `Person` class. A `Person` can have multiple meetings, managed through a `UniqueMeetingList` that ensures no duplicate meetings are associated with a person.

Each `Meeting` contains:
* `meetingDate` &mdash; a `LocalDate` object representing the date of the meeting.
* `meetingTime` &mdash; a `LocalTime` object representing the time when the meeting starts.
* `duration` &mdash; an `int` representing the duration of the meeting in minutes.
* `agenda` &mdash; a `String` detailing the agenda of the meeting.
* `notes` &mdash; an optional `String` for additional notes about the meeting.

Key functionalities provided by the meeting feature include scheduling, rescheduling, and canceling meetings. These are executed by the `ScheduleCommand`, `RescheduleCommand`, and `CancelCommand` classes, respectively.

`Meeting` interacts with the `Model` to ensure that the added meeting does not conflict with existing meetings for a person. This is done using `Model#hasMeeting` and `Model#getFilteredMeetingList`.

Here is a class diagram that shows the relationship between `Person` and `Meeting`:

<puml src="diagrams/MeetingClassDiagram.puml" />

An example usage scenario for scheduling a meeting is as follows:

Step 1: The user launches the application and the `Ui` loads up.

Step 2: The user inputs a command to schedule a meeting with a specific person in the list using the `schedule` command.

Step 3: The `LogicManager` takes the user input and parses it using `ScheduleCommandParser`, which in turn creates a `ScheduleCommand` object.

Step 4: `ScheduleCommand#execute` is called, which attempts to create a new `Meeting` and add it to the person specified.

Step 5: The `Model` checks if the meeting conflicts with any existing meetings for that person.

Step 6: If no conflicts are found, the new meeting is added, and the display is updated accordingly.

The sequence diagram below shows how the `schedule` command works within the `Logic` component:

<puml src="diagrams/ScheduleSequenceDiagram.puml" />

The activity diagram below summarizes the process of scheduling a meeting:

<puml src="diagrams/ScheduleActivityDiagram.puml"/>

#### Design considerations:

**Aspect: Handling of Meeting Overlaps**
* **Alternative 1 (current choice):** Meetings are strictly non-overlapping.
    * Pros: Simple rule that is easy to understand and implement.
    * Cons: Does not allow for flexibility in scheduling back-to-back meetings.
* **Alternative 2:** Allow overlapping meetings but provide warnings to users.
    * Pros: More flexibility in scheduling, can accommodate back-to-back meetings.
    * Cons: Increased complexity in the system, potential for user confusion.

**Aspect: Managing Meeting Lifecycle**
* **Alternative 1 (current choice):** Automatically delete past meetings.
    * Pros: Keeps the system clean and focused on future events.
    * Cons: Users lose the history of past meetings which might be needed for reference.
* **Alternative 2:** Keep past meetings and provide a mechanism to archive them.
    * Pros: Retains meeting history for records and future reference.
    * Cons: Might lead to clutter and require additional features to manage the archive.




### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* insurance agents
* has a need to manage a significant number of contacts
* needs to differentiate between different types of contacts
* track the status of contacts
* prefer desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: Provides fast access to client contact details, easily manage client relationships, collaborate with industry partners, and stay organised in a fast-paced industry.


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a…​                                    | I want to…​                      | So that I can…​                                                         |
|----------|-------------------------------------------|---------------------------------|-------------------------------------------------------------------------|
| `* * *`  | fast typing user                         | have a CLI                                               | use the app more efficiently                                            |
| `* * *`  | busy user                                | clear all my data quickly                                | quickly restart a new list of contacts                                  |
| `* * *`  | intermediate user                        | see my relationship with my contacts                     | know who they are                                                       |
| `* * *`  | intermediate user                        | find a certain client based on a keyword                 | easier to find the client                                               |
| `* * *`  | intermediate user                        | save and retrieve information                           | use the app in multiple sessions                                        |
| `* * *`  | intermediate user                        | add data about clients to the application               | record information about my client                                      |
| `* *`    | exploring the app                        | see what the app will look like with sample data          | more easily understand the potential features                           |
| `* *`    | exploring the app                        | have a tutorial of the basic features                     | get started with using the app                                          |
| `* *`    | exploring the app                        | access a help page with basic commands                    | familiarize myself with how to use the app                              |
| `* *`    | exploring the app                        | track my client information                               | manage my work using the app more efficiently                           |
| `* *`    | exploring the app                        | access past activity since app installation              | see my current progress                                                 |
| `* *`    | intermediate user                        | save similar client information data into a group        | manage them easily                                                      |
| `* *`    | intermediate user                        | reorganize my list of contacts                           | access them more clearly and efficiently                                |
| `* *`    | intermediate user                        | edit my contacts as a group                              | easier to implement changes if something in common in the group changes |
| `* *`    | careless user                            | have an undo command                                     | prevent doing some mistake                                              |
| `* *`    | intermediate user                        | filter client information                                | sort my client information according to some condition                  |
| `*`      | intermediate user                        | record if I am successful in securing an agreement       | know I have succeeded at my job                                         |
| `*`      | busy user                                | autocomplete my commands                                 | type commands faster                                                    |
| `*`      | intermediate user                        | tag my clients                                           | classify common groups                                                  |
| `*`      | intermediate user                        | have common use shortcut keys command                    | access the app more efficiently                                         |
| `*`      | intermediate user                        | track progress of engagements via tags                   | monitor the progress of different engagements with clients              |
| `*`      | intermediate user                        | copy information/features to another client              | reduce the time used                                                    |
| `*`      | intermediate user                        | quickly differentiate between clients and business partners | differentiate between them                                              |
| `*`      | intermediate user                        | rate clients for effective feedback                     | provide feedback efficiently                                            |
| `*`      | intermediate user                        | customize the app’s theme                                | better suits my preferences                                             |
| `*`      | expert user                              | create shortcuts for tasks                               | save time on frequently performed tasks                                 |
| `*`      | long-time user                           | archive/hide unused data                                 | not distracted by irrelevant data                                       |
| `*`      | frequent user                             | have templates for adding contacts                       | contacts are standardized and easier to read                            |
| `*`      | frequent user                             | schedule weekly check-ins with clients                   | do not forget about them                                                |
| `*`      | expert user                              | have reminders for meetings with clients                 | organize and plan my time well                                          |
| `*`      | expert user                              | create automated task workflows                          | save time on performing repeated tasks                                  |
| `*`      | expert user                              | see a competency rating based on past successes         | know if I need to improve                                               |
| `*`      | expert user                              | find clients based on different filters                  | better focus on one particular group                                    |
| `*`      | expert user                              | disable unnecessary features                             | the application is more customized and simpler to use                   |


### Use cases

(For all use cases below, the **System** is the `AddressBook` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Delete a person**

**MSS**

1.  User requests to list persons
2.  AddressBook shows a list of persons
3.  User requests to delete a specific person in the list
4.  AddressBook deletes the person

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.

**Use case: List out the contact information**

**MSS**

1.  User requests to list persons
2.  AddressBook shows a list of persons

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

**Use case: Filter the list of clients**

**MSS**

1.  User requests to filter list of persons
2.  AddressBook shows a list of persons that satisfy the filter

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

**Use case: Add a new person**

**MSS**

1.  User requests to add a new person.
2.  AddressBook prompts the user to enter the details of the person.
3. User enters the details of the person.
4. AddressBook adds the new person to the list.

   Use case ends.

**Extensions**

* 3a. The user enters invalid details.

    * 3a1. AddressBook shows an error message.
    * 3a2. AddressBook prompts the user to enter the details again.
    
      Use case resumes at step 2.

**Use case: Update a person's details**

**MSS**

1.  User requests to list persons..
2.  AddressBook shows a list of persons.
3. User requests to update the details of a specific person in the list.
4. AddressBook prompts the user to enter the new details.
5. User enters the new details.
6. AddressBook updates the person's details.

   Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid..

    * 3a1. AddressBook shows an error message.
  
      Use case resumes at step 2.

* 5a. The user enters invalid details.

    * 5a1. AddressBook shows an error message. 
    * 5a2. AddressBook prompts the user to enter the details again.
      
      Use case resumes at step 4.
  
**Use case: Clear AddressBook**

**MSS**

1. User requests to clear the AddressBook
2. AddressBook shows the empty AddressBook

    Use case ends

**Use case: Add a new policy**

**MSS**

1. User requests to list persons.
2. AddressBook shows a list of persons.
3. User requests to add policy to a specific person in the list.
4. AddressBook prompts the user to enter the details of the policy.
5. User enters the details of the policy.
6. AddressBook updates the person's policy details.

   Use case ends.

**Extensions**

* 2a. The list is empty.

    Use case ends.

* 3a. The given person index is invalid.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.

* 5a. The user enters invalid details.

    * 5a1. AddressBook shows an error message.
    * 5a2. AddressBook prompts the user to enter the details again.
    
      Use case resumes at step 4.


**Use case: Edit a policy**

**MSS**

1. User requests to list persons.
2. AddressBook shows a list of persons.
3. User requests to edit a policy of a specific person in the list.
4. AddressBook prompts the user to enter the details of the policy.
5. User enters the details of the policy.
6. AddressBook updates the person's policy details.

   Use case ends.

**Extensions**

* 2a. The contact list or policy list is empty.

  Use case ends.

* 3a. The given person index or policy index is invalid.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.

* 5a. The user enters invalid details.

    * 5a1. AddressBook shows an error message.
    * 5a2. AddressBook prompts the user to enter the details again.
      
      Use case resumes at step 4.

**Use case: Delete a policy**

**MSS**

1. User requests to list persons.
2. AddressBook shows a list of persons.
3. User requests to delete a policy of a specific person in the list.
4. AddressBook prompts the user to enter the details of the policy.
5. User enters the details of the policy.
6. AddressBook deletes the specified policy of the person.

   Use case ends.

**Extensions**

* 2a. The contact list or policy list is empty.

  Use case ends.

* 3a. The given person index or policy index is invalid.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.

* 5a. The user enters invalid details.

    * 5a1. AddressBook shows an error message.
    * 5a2. AddressBook prompts the user to enter the details again.
      
      Use case resumes at step 4.

**Use case: Find person in the contact list**

**MSS**

1. User requests to find a particular person in the contact list.
2. AddressBook prompts the user to enter the keywords to find the person.
3. User enters the keywords that match the person.
4. AddressBook list out all the person that match the keywords provided.

   Use case ends.

**Extensions**

* 3a. The user enters invalid details.

    * 3a1. AddressBook shows an error message.
    * 3a2. AddressBook prompts the user to enter the details again.
      
      Use case resumes at step 2.

**Use case: Increment a client's status**

**MSS**

1. User requests to list persons.
2. AddressBook shows a list of persons.
3. User requests to change a particular client's status in the contact list.
4. AddressBook prompts the user to enter direction of status change.
5. User enters the direction of status change.
6. AddressBook changes the client's status and updates the progress dashboard.

   Use case ends.

**Extensions**

* 2a. The contact list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. AddressBook shows an error message.

      Use case resumes at step 2.

* 5a. The user enters an invalid direction.

    * 5a1. AddressBook shows an error message.
    * 5a2. AddressBook prompts the user to enter the direction again.

      Use case resumes at step 4.


### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse. 
4. The response to any use action should become visible within 5 seconds.
5. Should have a user-friendly interface that is easy to navigate and understand.
6. Should be stable and not crash or lose data under normal use.
7. Should be able to handle increasing amounts of data and users without significant degradation in performance (Scalability).
8. Should protect sensitive data and prevent unauthorized access, ensuring data integrity and confidentiality (Security).
9. Should be easy to maintain, with clear documentation and a modular design that allows for easy updates and fixes.
10. Should be accessible to users with disabilities, following guidelines such as the Web Content Accessibility Guidelines (WCAG).


*{More to be added}*

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Private contact detail**: A contact detail that is not meant to be shared with others

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   2. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

2. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   2. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

3. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   2. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   3. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   4. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

2. _{ more test cases …​ }_

### Changing a client's status

1. Incrementing a client's status

    1. Prerequisites: List at least one client using the `list` or `find` command. The first person should be a client and the status should be `Yet to start`.

    2. Test case: `status 1 s/up`<br>
       Expected: Client status of the first person is updated to `In progress`. Details of the person shown in the status message. In the progress bar, the value for `Yet to start` decreased by 1 and the value for `In progress` increase by 1.

    3. Test case: `status 0 s/up`<br>
       Expected: No person is changed. Error details shown in the status message. Progress bar remains the same.

    4. Test case: `status 1 s/right`<br>
       Expected: No person is changed. Error details shown in the status message. Progress bar remains the same.

    5. Other incorrect status commands to try: `status`, `status x s/up`, `...` (where x is larger than the list size)<br>
       Expected: Similar to previous.

2. Resetting a client's status

    1. Prerequisites: List at least one client using the `list` or `find` command. The first person should be a client.

    2. Test case: `status 1 s/`<br>
       Expected: Client status of the first person is reset to `Yet to start`. Details of the person shown in the status message. In the progress bar, the value for `Yet to start` increased by 1 if the person's previous status was not `Yet to start`.

    3. Test case: `status 0 s/`<br>
       Expected: No person is changed. Error details shown in the status message. Progress bar remains the same.

    4. Other incorrect status commands to try: `status s/`, `status x s/`, `...` (where x is larger than the list size)<br>
       Expected: Similar to previous.

### Adding a policy

1. Adding a policy to a person while all persons are being shown
    1. Prerequisites: List all persons using the list command. Multiple persons in the list.

    2. Test case: `policy 1 po/Policy ABC`<br>
       Expected: Add a policy named `Policy ABC` to the first person. Details of the edited contact are shown in the status message. The timestamp in the status bar is updated.

    3. Test case: `policy 0 po/Policy ABC`<br>
       Expected: No person added the policy given. Error details are shown in the status message. The status bar remains the same.

    4. Other incorrect adding policy commands to try: `policy`, `policy 1 po/`, `policy x po/Policy ABC` (where x is larger than the list size)<br>
       Expected: Similar to previous.

2. _{ more test cases …​ }_

### Editing a policy

1. Editing a policy to a person while all persons are being shown
    1. Prerequisites: List all persons using the list command. Multiple persons in the list.

    2. Test case: `policy 1 pi/2 po/Policy ABC`<br>
       Expected: Edit the policy named `Policy ABC` to the first person. Details of the edited contact are shown in the status message. The timestamp in the status bar is updated.

    3. Test case: `policy 1 pi/0 po/Policy ABC`<br>
       Expected: No person edits the policy given. Error details are shown in the status message. The status bar remains the same.

    4. Other incorrect adding policy commands to try: `policy 1 pi/`, `policy pi/1 po/`, `policy x pi/y po/Policy ABC` (where x is larger than the list size, y is larger than the policy list size)<br>
       Expected: Similar to previous.

2. _{ more test cases …​ }_

### Deleting a policy

1. Deleting a policy to a person while all persons are being shown
    1. Prerequisites: List all persons using the list command. Multiple persons in the list.

    2. Test case: `policy 1 pi/2 po/`<br>
       Expected: Delete the second policy of the first person. Details of the edited contact are shown in the status message. The timestamp in the status bar is updated.

    3. Test case: `policy 1 pi/0 po/`<br>
       Expected: No person deletes the policy. Error details are shown in the status message. The status bar remains the same.

    4. Other incorrect adding policy commands to try: `policy x pi/y po/` (where x is larger than the list size, y is larger than the policy list size)<br>
       Expected: Similar to previous.

2. _{ more test cases …​ }_

### Adding a person

1. Adding a person to the contact list

    1. Test case: `add n/John Doe p/98765432 e/johnd@example.com a/311, Clementi Ave 2, #02-25 r/client t/friends t/owesMoney`<br>
       Expected: Add this person to the contact list. Details of the added contact are shown in the status message. The timestamp in the status bar is updated.

    1. Test case: `add n/John Doe p/98765432 e/johnd@example.com a/311, Clementi Ave 2, #02-25 r/dummy t/friends t/owesMoney`<br>
       Expected: No person is added. Error details are shown in the status message. The status bar remains the same.

    1. Other incorrect add commands to try: `add n/John Doe`, `add n/ ...`, `add n/1234 ...`, `add` <br>
       Expected: Similar to previous.

1. _{ more test cases …​ }_

### Finding a person

1. Find a person from the contact list

    1. Test case: `find n/Alex`<br>
       Expected: List all person that contains the name `Alex` in the contact list. Successful message of find command shown in the status message. The timestamp in the status bar is updated.

    1. Test case: `find n/`<br>
       Expected: The contact list remains unchanged. Error details are shown in the status message. The status bar remains the same.

    1. Other incorrect add commands to try: `find n/Hans Bo`, `find`, `find 123` <br>
       Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

2. _{ more test cases …​ }_

***

## **Appendix: Effort**

### Difficulty level

We felt that the project was considerably difficult.
We were initially ambitious and wanted to create a product that had multiple different features to solve the different user stories we had.
Even after looking through and only focusing on the most important user stories, the features that we had to implement were still very elaborate and required a lot of time and effort.
At the start, we also did not account for the other requirements such as the User Guide and Developer Guide which also took up time and effort.
<br>

### Challenges faced

We faced several challenges along the way during the project.

First was quickly understanding how AB3 was structured and how we could add features to it.
For most of us, it was our first experience working with such a huge codebase and there were many individual components working together.
We had to quickly figure out how to implement our features that had parts in the Logic, Model, and Ui segments.
The tutorial provided was helpful, but some of our features had parts that went beyond the scope of the tutorial that we had to find out how to implement ourselves.

We also had to work out the kinks when working in a team and using the GitHub workflow to organise our changes.
We were largely unfamiliar with working with a codebase in a team and so followed the GitHub workflow quite closely to avoid any mishaps.
However, since it was also our first time working together, we made some critical mistakes when trying to merge our code together and had to spend some time trying to resolve these issues.
There were also times when it was difficult to split the workload in a way that our changes would not overwrite what the rest contributed.
<br>

### Effort required

We feel that we put in a lot of effort over the course of this project.

As our product is tailored towards insurance agents, we had to significantly change the Model component from AB3 which were more for generic people.
We had to define the relationships a person could have, and then build the new fields that each person could have such as Policy and Meeting, where each of them also had their own details to track.
We also significantly updated the UI to be more useful and intuitive for our target users. Creating the additional panels and drop-down functionalities required more advanced JavaFX than AB3 and we spent time and effort to implement it the best way possible.
As our features are also quite advanced, we faced many different bugs close to the end of the project ranging from functionality issues to UI inconsistencies which we had to quickly identify and fix.
Our User Guide and Developer Guide then also added to our workload as we had to explain each feature in depth.
However, we were able to split the workload well and pulled through.
<br>

### Achievements

As a result of our efforts, we are proud of being able to create a product which significantly builds upon AB3.
Our features are detailed, useful for our target users and work well together to create a complete product. Most of the user stories that we identified at the start are solved, especially the ones we deem as high priority.
We believe that our UI is very user-friendly and intuitive, and is likely something that is beyond the normal expectations for this course.
While there is still room for improvement, we feel that currently InsuraConnect is a good representation of our efforts thus far.
<br>

***

## **Appendix: Planned Enhancements**
Team size: 4
1. Separate the three actions of adding, editing, and deleting policies into three different commands. Currently, the `policy` command handles these three actions, which can be quite confusing for users to differentiate. We plan to create three commands: `addpo`, `editpo`, and `delpo`, to handle each action clearly.
2. The current `find` command can only find the full word based on the prefix. For example, `find n/Ben` will only list "Ben" and not "Benson." We plan to modify it to accept partial words so that users don't have to input the full word to search for something in the contact list. 
3. Allow special characters in name such as `s/o` and also in phone numbers such as `+65` to allow for country code for international contacts.
4. Include more stringent validity checks for duplicate contacts by checking against their phone number and emails and address instead of name.
