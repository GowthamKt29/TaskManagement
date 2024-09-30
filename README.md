# TaskManagement Android App

A simple and efficient TaskManagement application built with Jetpack Compose.
This app allows users to manage their tasks by creating, updating, deleting, and scheduling them to
show notifications.

## Features

- **View Tasks**: Display a list of tasks categorized by status.
- **Create New Task**: Add new tasks with details such as name, description, and time.
- **Update Tasks**: Edit existing tasks.
- **Delete Tasks**: Remove tasks from the list.
- **Task Scheduling**: Set reminders and schedule notifications for tasks.

## MVVM Architecture

This app follows the **Model-View-ViewModel (MVVM)** architecture pattern to ensure separation of
concerns and facilitate testability:

- **Model**: Responsible for handling data (Room database) and business logic.
- **View**: The Jetpack Compose UI, which observes data and reacts to changes.
- **ViewModel**: Manages the communication between the View and Model, and provides observable data
  to the View.

## Screenshots

| Main Screen                          | Task Creation                             | Notifications                           |
|--------------------------------------|-------------------------------------------|-----------------------------------------|
| ![Main Screen](screenshots/home.png) | ![Task Creation](screenshots/addtask.png) | ![Notification](screenshots/notify.png) |

## Build with

- **Jetpack Compose**: Used for building declarative UI.
- **Room**: Local database for persisting tasks.
- **Dagger Hilt**: Dependency injection framework.
- **Coroutines**: Asynchronous programming to handle background tasks.
- **Flow**: Handle data streams and state management.
- **WorkManager**: Schedule tasks for showing notifications.

### Testing

- **JUnit**: Framework for unit testing.
- **Mockito**: Used for mocking objects during tests.
- **Coroutines Test**: Testing coroutine-based code.

## Developed By

Gowtham A

## License

```plaintext
Copyright 2024 (GOWTHAM ASHOK)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
