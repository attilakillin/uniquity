FROM uniquity-base
ARG username=default

RUN addgroup -S $username && adduser -S $username -G $username
USER $username
