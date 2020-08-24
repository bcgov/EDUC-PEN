FROM api-base-spring-boot-2.3.2-template:latest as build
WORKDIR /workspace/app

COPY api/pom.xml .
COPY api/src src
RUN mvn package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:11-jdk
RUN useradd -ms /bin/bash spring
RUN mkdir -p /logs
RUN chown -R spring:spring /logs
RUN chmod 755 /logs
USER spring
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-Duser.name=DIGITALID_API","-Xms512m","-Xmx512m","-noverify","-XX:TieredStopAtLevel=1","-XX:+UseParallelGC","-XX:MinHeapFreeRatio=20","-XX:MaxHeapFreeRatio=40","-XX:GCTimeRatio=4","-XX:AdaptiveSizePolicyWeight=90","-XX:MaxMetaspaceSize=100m","-XX:ParallelGCThreads=1","-Djava.util.concurrent.ForkJoinPool.common.parallelism=1","-XX:CICompilerCount=2","-XX:+ExitOnOutOfMemoryError","-Djava.security.egd=file:/dev/./urandom","-Dspring.backgroundpreinitializer.ignore=true","-cp","app:app/lib/*","ca.bc.gov.educ.api.digitalid.DigitalIDApiResourceApplication"]
