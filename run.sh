
cd container
mvn install
cd ..

cd jpadomain
mvn install
cd ..

cd backend
mvn install
cd ..

java -jar backend-0.1.jar

