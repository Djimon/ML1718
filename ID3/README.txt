#Team: Christoph Dollase and Kilian Pößel

#Answer the questions:
1. What is your result tree?
Unfortunately our result tree is much to big. We have one long path ending in a repeating sequence. So the most common reason would be that we have an endless recursion.
Due to lack of time we couldn't fix the problem in our code.
So we decided to dirty cut the recursion at a fixed depth.
Some possible locations for the errors are:
 - calculation of the entropy
 - checking if the tree is pure
 - adding of children (leafs)
But at least

2. How are the instances distributed in the leaf nodes?
Unfortunately in our tree the leaf nodes are missing.

#DOCU:
First step of our programm is reading the data out of given car data set. We implemented a simple parser that splits the text per line and per delimiter (","). This method generates an ArrayList of Type Car with all the Cars with their attributes. So we designed our Car class with this attributes and used global enums for each of these.
the nexr part is the building of the tree. Due to some Problems with calling methods from generic types from the ArraList we had to do a workaround by implementing a car subclass and working on copies.
the tree building function calculates the current entropy of the tree and after that the best attribute, that gives the highest gain. The actual tree node is then splittet for that attribute and we proceed to add the next children recursively if the tree is not yet pure. (Here we recognized some problems, so we implemented a limit of depth in the recursion. We did this to be able to have at least some test runs and analyse our results. in the end there wasn't enough time to fix these problems.)
With the construction of the tree finnished we implemented our own class to convert the tree structure in to the XML format.
the function is parsing through the tree and printing the results in the given format and saving it into a file.

#P.S: We apologize for submitting an unfinished task. We didn't calculate with having such big trouble on the tree. Hopefully we get some points for the correct XML-format and the documentation
