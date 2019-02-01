DOCKER_IMAGE_TAG        ?= $(subst /,-,$(shell git rev-parse --abbrev-ref HEAD))
DOCKER_REPO             ?= daesu
DOCKER_IMAGE_NAME		?= spark
	
.PHONY: docker
docker:
	docker build -t "$(DOCKER_REPO)/$(DOCKER_IMAGE_NAME):$(DOCKER_IMAGE_TAG)" .

.PHONY: docker-run
docker-run:
	docker run -it -p 4567:4567 $(DOCKER_IMAGE_NAME)

.PHONY: tests
tests:
	gradle test

.PHONY: run
run:
	docker-compose up

.PHONY: docker-publish
docker-publish:
	docker push "$(DOCKER_REPO)/$(DOCKER_IMAGE_NAME)"

.PHONY: docker-tag-latest
docker-tag-latest:
	docker tag "$(DOCKER_REPO)/$(DOCKER_IMAGE_NAME):$(DOCKER_IMAGE_TAG)" "$(DOCKER_REPO)/$(DOCKER_IMAGE_NAME):latest"

.PHONY: docker-login
docker-login:
	docker login -u $(DOCKER_LOGIN) -p $(DOCKER_PASSWORD) $(DOCKER_REPO)