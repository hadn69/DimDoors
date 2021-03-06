package org.dimdev.dimdoors.shared.pockets;

import org.dimdev.dimdoors.shared.rifts.RiftDestination;
import org.dimdev.dimdoors.shared.rifts.destinations.AvailableLinkDestination;
import org.dimdev.dimdoors.shared.rifts.destinations.PocketEntranceMarker;
import org.dimdev.dimdoors.shared.rifts.destinations.PocketExitMarker;
import org.dimdev.dimdoors.shared.rifts.registry.LinkProperties;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public final class DefaultDungeonDestinations { // TODO: lower weights?

    public static final LinkProperties pocketLinkProperties = LinkProperties.builder()
            .groups(new HashSet<>(Arrays.asList(0, 1)))
            .linksRemaining(1).build();

    public static final LinkProperties overworldLinkProperties = LinkProperties.builder()
            .groups(new HashSet<>(Arrays.asList(0, 1)))
            .entranceWeight(50)
            .linksRemaining(1).build();

    public static final RiftDestination deeperDungeonDestination = AvailableLinkDestination.builder()
            .acceptedGroups(Collections.singleton(0))
            .coordFactor(1)
            .negativeDepthFactor(10000)
            .positiveDepthFactor(160)
            .weightMaximum(100)
            .newRiftWeight(1).build();

    public static final RiftDestination shallowerDungeonDestination = AvailableLinkDestination.builder()
            .acceptedGroups(Collections.singleton(0))
            .coordFactor(1)
            .negativeDepthFactor(160)
            .positiveDepthFactor(10000)
            .weightMaximum(100)
            .newRiftWeight(1).build();

    public static final RiftDestination overworldDestination = AvailableLinkDestination.builder()
            .acceptedGroups(Collections.singleton(0))
            .coordFactor(1)
            .negativeDepthFactor(0.00000000001) // The division result is cast to an int, so Double.MIN_VALUE would cause an overflow
            .positiveDepthFactor(Double.POSITIVE_INFINITY)
            .weightMaximum(100)
            .newRiftWeight(1).build();

    public static final RiftDestination twoWayPocketEntrance = PocketEntranceMarker.builder()
            .weight(1)
            .ifDestination(PocketExitMarker.builder().build())
            .otherwiseDestination(AvailableLinkDestination.builder()
                    .acceptedGroups(Collections.singleton(0))
                    .coordFactor(1)
                    .negativeDepthFactor(80)
                    .positiveDepthFactor(10000)
                    .weightMaximum(100)
                    .newRiftWeight(1).build()).build();

    public static final RiftDestination gatewayDestination = AvailableLinkDestination.builder()
            .acceptedGroups(Collections.singleton(0))
            .coordFactor(1) // TODO: lower value?
            .negativeDepthFactor(Double.POSITIVE_INFINITY)
            .positiveDepthFactor(160) // TODO: lower value?
            .weightMaximum(300) // Link further away
            .newRiftWeight(1)
            .build();
}
