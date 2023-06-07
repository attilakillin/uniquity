# Add an extra layer to the built backend application,
# which creates a custom user with a specified name.

# Argument, which image to extend.
ARG image

FROM $image
# Argument, what should the new user's name be.
ARG username=default
# Add group and user.
RUN addgroup -S $username && adduser -S $username -G $username
# Set user as active.
USER $username
