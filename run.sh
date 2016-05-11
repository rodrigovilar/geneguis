
cd container
mvn install
cd ..

cd jpadomain
mvn install
cd ..

cd backend
mvn install
cd ..

cd e2e/src/main/resources/lib
mvn install:install-file -Dfile=JSErrorCollector-0.6.jar -DgroupId=strepo.ext -DartifactId=JSErrorCollector -Dversion=0.6 -Dpackaging=jar
cd ../../../../..

