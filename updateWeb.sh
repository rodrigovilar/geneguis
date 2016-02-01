cd frontend
mvn coffeescript:compile
cd ..

cp -r frontend/src/main/webapp/* e2e/src/main/webapp/
cp -r widgets/src/main/resources/templates/* e2e/src/main/resources/widgets/

