Kudu “Waterhole” (intermediary/REST API)
========================================
The goal is to make it easier to interact with Kudu. For instance, if I wanted to send data (e.g. GPS data) straight from my phone/smart-device to a Kudu cluster, I would have to have Kudu installed on that device just to use the client. Alternatively, data would go through an intermediary, an in-between step that would handle interacting with Kudu while exposing a easy-to-use public interface.


[11:51 AM] Adar Dembo: kudu <--> intermediary[a][b] <--> phone

[11:52 AM] Adar Dembo: the intermediary and the phone would speak HTTP or whatever

[11:52 AM] Adar Dembo: the intermediary and kudu would speak the Kudu client protocol


* Notes:
   * Adar pointed out the possibility of this being a potential third daemon in Kudu’s architecture, rather than just master and tserver, there would be a third: REST[c][d] client (or something)
      * Probably future work. For now, we should focus on getting something up and running
   * Also noted the desire to make this a more complete client, rather than just a facilitator for ingesting data
      * Also probably future work, but we can structure this smartly so it would be easy to add on in the future
   * Java may be preferable for this type of server.  Java APIs here.
      * One argument for only doing data ingestion is that querying well would likely depend on C++ (Kudu+Impala)
      * Maybe a separate REST client in C++ that acts like Impala, or even a REST client for Impala
         * E.g. Submit Impala query over REST
   * If we want to scale horizontally to many of these, would the thing to do be to register every REST client with each other and to redirect if busy?
      * Could also send over a status of each server when trying to redirect


* Why “Waterhole”?
   * Kudus get water from the waterhole, Kudu gets data from the intermediary
* Current ways to interact with client:
   * C++
   * Java
   * Python (experimental)
   * Impala (C++/Java “Frankenstein”)
   * Rust (from Dan)
   * Missing? REST!
* +s:
   * Language agnostic because it’s REST
   * Easy connection to cluster
   * Easily extendable based on specific application needs
* Necessary components:
   * Register a client with the master
   * Config file to define the schema?
      * Query the master to determine the schema?
   * Support (at least):
      * Connect with cluster
         * Determine schema
      * Insert
      * Update
      * Upsert
      * Delete
* Questions:
   * Why wouldn’t they just talk to the cluster directly?
      * Currently, data can be put into Kudu by using the client APIs (Java, C++, Python), but this puts the burden on the client application to make/build Kudu and then send over data
         * The Waterhole would be like an add-on to the cluster, loading data directly to the master and tablets
         * Applications would be able to connect to the Waterhole over HTTP (or something) and push data to it
   * What is NOT included?[e]
      * Queries
         * We are assuming that any data read would occur due to an analytical query, which generally happens on heavier-powered devices. The benefit of Waterhole is that it can be done on light-weight devices
         * Speeds up development
      * Creating new tables
   * How would we scale this up?
   * Stateless or stateful ?
      * Probably stateless?

* Update (7/8/2016):
	* Currently supporting create tables, scans, and inserts
	* TODOs: update the JSON support to be ProtoBuf by default and internal conversions to JSON and XML [b]
		* HBase's REST API is centered around ProtoBuf


[a]see http://blog.cloudera.com/blog/2013/03/how-to-use-the-apache-hbase-rest-interface-part-1/

[b]https://github.com/apache/hbase/blob/master/hbase-rest/src/main/java/org/apache/hadoop/hbase/rest/RESTServer.java

[c]https://jersey.java.net/

[d]This is the framework used by HBase

[e]This was how I originally formulated the problem, to be easy for applications to push data to Kudu. There is need beyond this to make the client easier to work with.
