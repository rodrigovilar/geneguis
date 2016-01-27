
curl -vX POST http://localhost:8080/widgets -d @widgets/src/main/resources/js/$1.json --header "Content-Type: application/json"

curl -vX POST http://localhost:8080/widgets/$1/code -d @widgets/src/main/resources/js/$1.js --header "Content-Type: application/json"

