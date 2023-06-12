# Add extra layers to the built backend application, adding extra functionality.

# Argument, which image to extend.
ARG image

# 1: Install systemd files so that the backend can access them.
FROM docker.io/centos/systemd AS systemd-files


# 2: Create a custom user with a specified name, and copy systemd files.

FROM $image
# Argument, what should the new user's name be.
ARG username=default
# Add group and user.
RUN addgroup -S $username && adduser -S $username -G $username
# Set user as active.
USER $username

# Copy systemd files
COPY --from=systemd-files /usr/lib/systemd /systemd
