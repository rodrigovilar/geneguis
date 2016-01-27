cd frontend
mvn coffeescript:compile
cd ..

cd widgets
mvn coffeescript:compile
cd ..

cp -r frontend/src/main/webapp/* backend/src/main/webapp/
cp -r widgets/src/main/resources/js/* backend/src/main/resources/widgets/

