# Keep the track of your purchases

The **MyCheque Application** allows you to save your market receipts into a storage and retrieve detailed information about your purchases using the flexible set of search parameters.

## How to install

**The only dependency you need is _Docker._** \
For Windows, install [Docker Desktop](https://docs.docker.com/desktop/setup/install/windows-install/). \
To make Docker work, you might also need to have [WSL](https://learn.microsoft.com/en-us/windows/wsl/install) installed.

## How to try it out

1) Create a directory for the application files, for instance,
    ```
    C:\Projects\MyChequeApplication
    ```

2) From the [repository](https://github.com/resxnvnce/mycheque-application/tree/release) root, **download _each_ of the listed files**:
    ```
    pg-init.sql
    docker-compose.yml
    ```

3) Move these files to the directory you created at step one.

4) **Start _Docker Desktop_, if _Docker_ has not been started yet.**

5) Open `cmd` (the command prompt) and move to application directory:
    ```
    $ cd C:\Projects\MyChequeApplication
    ```
    and then, if you're ready to run the application, execute
    ```
    $ docker compose up -d
    ```
    At this point, Docker downloads all necessary images and then builds the application, starting a bunch of containers. \
    **This might take some time**.

6) Once the logs like these show up: \
    ![startup logs](/images/startup-logs.PNG) \
    meaning the application has started, you can access the **API**.

7) To stop the application, you have to execute
    ```
    $ docker compose down
    ```
    or
    ```
    $ docker compose down -v
    ```
    The difference is the second variant removes **all the data you might have saved** by sending various requests to the **API**. \
    So, **for a continuous usage of the application**, consider using the first command.

## The application is running. What's next?
1) In your browser, open the link
    ```
    http://localhost:8080/swagger-ui/index.html#/
    ```
    **This leads to a _Swagger UI_ web interface, simplifying any interaction with the API.**
2) To save a receipt, you need to create a personal _profile_. \
    A _profile_ is created via the endpoint:
    ```
    POST /mycheque.com/v1/customers
    ```
3) As you may have noticed, in order to create a _profile_, **you need _a token_**. This is a trusted third-party API token required for the application to retrieve detailed receipt information. \
    To obtain one, visit [this page](https://proverkacheka.com/) and register. After the registration, **you can generate _a personal API key_**.
4) **Use the _API key_ to create a _profile_** by using the endpoint, that was mentioned earlier.
5) At this point, the **MyCheque API** can be used to save your receipts and perform dynamic search queries. \
    See the documentation in the web interface for more.
