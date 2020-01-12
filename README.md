Products & Categories
-----
A very simple android application to display a list of categories of products. This app enables us to browse products and filter them based on different categories.

## Project Structure
The project is based on the MVVM design pattern. Besides the main design pattern, we're using the *package by layer* idea
to break the codebase into more manageable parts as following:
 - The `adapter` package encapsulates the relationship between data and views.
 - The `component` package encapsulates the top-level fragments and the `ui` package encapsulates the remaining fragments.
 - The `config` package is responsible for externalizing configurations. This way we can simply switch between different configurations
 in different environments.
 - The `model` package is encapsulating the domain entities of this project, e.g. `Product` and `Category`.
 - The `network` package encapsulates all our remote calls.
 - The `MainActivity` contains and composes all other fragments.
 
## Patterns & Ideas
 - MVVM to streamline the relationship between view components, data components and application logic.
 - Singleton: To create one instance of a few classes like `ServiceGenerator`. We've the famous `Lazy Initialization Holder` to implement
 such classes.
 - Builder Pattern
 - TDD

## Test Structure
Since we don't have any truly isolated logic, we don't have any Unit Tests for this project. On the contrary, we do have quite a lot of UI tests developed using the following technologies and ideas:
 - Espresso: The UI testing framework developed by Google.
 - Wiremock: To mimic different behaviors of a REST API. For instance, we can simply test timeouts, empty JSONs, unexpected responses, etc.
 - Externalized Configurations: In order to have different configurations in test and normal environments, we're using a dedicated test profile.

### Running Tests
In order to run automated tests with the code coverage report, just run the following command:
```bash
./gradlew :app:createDebugCoverageReport
```
Please note that you will need a connected device for this to properly execute.

### Code Coverage Report
We're using the famous Jacoco utility to report the code coverage. While having a connected device, just run the following command:
```bash
./gradlew :app:createDebugCoverageReport
```
And then inspect the report residing in the `app/build/reports/coverage/debug/index.html` path and see how the code coverage looks like.

###  Built With
 - *Retrofit* to communicate with remote APIs
 - *Glide* to handle all image related stuff
 - *Espresso* to facilitate the UI testing process
 - *JUnit 4* as our testing framework
 - *WireMock* to mimic remote API behaviors in testing environments

License
---

    Copyright 2020 MZ

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS, 
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
