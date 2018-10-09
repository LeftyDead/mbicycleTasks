README 
=======================

Project name - mbicycleTasks

Used: Java 8, Gradle, MySQL

Run
------------
To run, use Gradle:

		gradle run
		
To specify arguments, use 

		gradle run --args "Task_Number_To_Launch[;Argument_1;Argument2...]"
			//or
		gradle run --args="Task_Number_To_Launch[;Argument_1;Argument2...]" 
		
Note: Every argument within quotes separates by character ';'

For Example:

		gradle run --args "1;1 + 2 $ 3 / 4 - 5"	//Will launch Task #1 with expression '1 + 2.5 $ 3 / 4 - 5' to calculate
		gradle run --args "2" //Will launch Task #2
		gradle run --args "3;jdbc:mysql://localhost:3306/;books;myUser;myPass;myAuthor" //Will launch Task #3 with 'mysql://localhost:3306/' as URL connection to MySQL Data Base, 'books' as Data Base name, 'myUser' as MySQL user (login), 'myPass' as MySQL password, 'myAuthor' as Author.
		
Important note
----------	
	In task #1, instead of multiplication sign '*' use '$' (for example: "2 * 2" is wrong and "2 $ 2" is right)
----------------------------------------------------------


Additional
----------

Directory Additional contains one single file (Info.txt) with SQL scripts and additional information.