build:
	echo "Clean build exclude test"
	./gradlew clean build -x test

e:
	echo "build eureka image"
	cd eureka-server && docker build -t fedomn/eureka-server .

r1:
	docker run -p 1111:1111 --env SERVICE_RUL=http://kubernetes.docker.internal:1112/eureka --env EUREKA_HOSTNAME=eureka-1 --name fedomn-eureka-1 fedomn/eureka-server

r2:
	docker run -p 1112:1111 --env SERVICE_RUL=http://kubernetes.docker.internal:1111/eureka --env EUREKA_HOSTNAME=eureka-2 --name fedomn-eureka-2 fedomn/eureka-server

compute:
	echo "build compute image"
	cd compute-service && docker build -t fedomn/compute-service .

c1:
	docker run -p 8889:8888 --env SERVICE_RUL=http://kubernetes.docker.internal:1112/eureka,http://kubernetes.docker.internal:1111/eureka --name fedomn-compute-1 fedomn/compute-service

c2:
	docker run -p 8888:8888 --env SERVICE_RUL=http://kubernetes.docker.internal:1112/eureka,http://kubernetes.docker.internal:1111/eureka --name fedomn-compute-2 fedomn/compute-service

feign:
	echo "build feign image"
	cd feign-service && docker build -t fedomn/feign-service .

f1:
	docker run -p 4445:4444 --env SERVICE_RUL=http://kubernetes.docker.internal:1112/eureka,http://kubernetes.docker.internal:1111/eureka --name fedomn-feign-1 fedomn/feign-service
