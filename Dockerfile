# -- Build step
FROM mozilla/sbt as builder

WORKDIR /build
ADD . /build
RUN sbt -Dsbt.ivy.home=.ivy2 -mem 1000 clean assembly

# -- Run step
FROM openjdk

WORKDIR /app
COPY --from=builder /build/assembly/hackathon-blockchain-explorer-*.jar hackathon-blockchain-explorer.jar
COPY --from=builder /build/docker .

# Exposed port must match the one defined in application.conf
EXPOSE 8080
CMD ["/app/run.sh"]
