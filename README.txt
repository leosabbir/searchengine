*********************************************************************************
This is a simple full text search based search engine done as a part of graduate course, Advanced Data Base.

This search engine is based on creation of "Inverted Index File". This inverted index file is used to find the 
search result. The project can run two types of search queries:

1. Boolean Search Query
2. Vector Space Search Query

********************************************************************************


The project is eclipse project and can be run by importing to the eclipse IDE.

OR

It can also be run by ANT.
You need have ANT installed for this.
Also JDK path has to added to system path.

 1. In terminal/console change directory to root of the project.
    Here is a file "build.xml"

 2. run command "ant"
 	e.g. searchEngine> ant
 	
 	This will run the program. 
 	
 
 ASSIGNMENT #1
 -------------	
 outputs to the output file --> "word_count_in_each_doc.txt"
 	

ASSIGNMENT #2
-------------
 outputs to the output file --> inverted_index_file.txt
 
 ASSIGNMENT #3
-------------
To launch boolean searcher, run
searchEngine> ant run_boolean_search

ASSIGNMENT #4
-------------
To launch Vector Space based search engine, run
searchEngine> ant run_vector_space_search
