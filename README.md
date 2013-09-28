done
====

A Clojure clone of idonethis.com

This is mostly an ongoing experiment to learn web development with clojure, but it's also fully functional
and ready to be installed in your own server

## Prerequisites

1) You will need [Leiningen][1] 1.7.0 or above installed.

[1]: https://github.com/technomancy/leiningen

2) You need to have access to a MySQL instance. In the /docs/database folder you find the scripts to create
the database

3) The src/ folder contains a configuration file called applicaton.conf where you need to specify the
address, username and password to connect to your MySql instance

## Running

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2013 Filippo Diotalevi 

This softawre is licensed under the terms of Apache Software License v.2.0.  See LICENSE file

