REST Client for Apache Kudu
===========================
The goal of the REST client is to make it easier to interact with the Kudu.
Currently, there is only C++, Java, and (experimentally) Python client support
for Kudu, limiting the client-side operations to those three languages. If a
developer wanted to upload data straight from their iPhone written in Swift to
a Kudu cluster, they would have to develop an additional wrapper around their
Kudu client to support interactions with Swift.

Additionally, the most developed client, the Java client, exposes an elaborate
asynchronous API, requiring developers to define callbacks and retry behaviors,
else use the synchronous API, which might not always be the desired behavior.

APIs
----
Asynchrnous:
Create table
Insert
Update
Delete

Synchronous:
Scan

Security
--------
Currently no security is implemented, but in the future, Kerberos tokens are a
likely candidate for authentication/authorization. Since the REST client will be
a trusted component of the Kudu environment, it should be able to run operations
as users, reflecting their account privileges.
