# Smart Routing Assistant

## Project Purpose
The **Smart Routing Assistant** aims to help users choose the most efficient routes based on their preferences such as avoiding walking, minimizing travel time, or saving money. The project started from a personal experience where I struggled to find the best route between **Marathahalli** and **Attipelli** using Google Maps. I wanted an assistant that would help me make the best choice based on my needs. This idea became the **Smart Routing Assistant**, an AI-powered assistant that suggests optimal routes for different modes of transport, including **bus**, **cab**, **Rapido**, and **walking**.

## The Problem
While using Google Maps, I realized that sometimes there are multiple options for a route, but none of them offer a clear recommendation based on the user's preferences. For example:
- **Traveling by bus:** You may need to walk for 3-5 minutes at different stops.
- **Cab/Taxi:** You might want to avoid walking but prefer to save time or money.

### A real-world example:
One day, I had to travel from **Marathahalli Flyover** to **Attipelli NMR Convention Hall**. I saw multiple routes:
- **Route 1**: Bus from Marathahalli to Silk Board, followed by a 3-5 minutes walking, and then a bus to Attipelli.
- **Route 2**: Bus from Marathahalli to Kyaarpuram, then directly to Attipelli, no walking involved.
- **Route 3**: Another bus + cab route, but my sister didn’t want to walk.

I needed an efficient route based on factors like **time** and **walking preference**, and it was difficult to figure out the best one using only the map. This experience made me think of creating an assistant that would help users choose the best route.

## How It Works
The **Smart Routing Assistant** helps you find the best route based on:
1. **Time-efficient route**: It chooses the fastest route by calculating travel times across different modes (e.g., bus, rapid, or cab).
2. **Cost-efficient route**: It calculates the cost for the route based on factors like distance and mode of transport.

Users can input their **source** and **destination**, and choose their preference (e.g., avoid walking, save time, or save money). The assistant then suggests **two routes**:
1. The best **time-efficient** route.
2. The best **cost-efficient** route.

### Example Workflow:
1. **Source**: Marathahalli
2. **Destination**: Attipelli
3. **Preference**: Time-efficient route (or cost-efficient)

The assistant provides:
- Option 1: **Time-efficient** route with modes and travel details.
- Option 2: **Cost-efficient** route with modes and travel details.

## Unique Features:
- **Personalized route recommendations**: By choosing between time and cost preferences, the assistant offers customized route suggestions.
- **Cab pricing integration**: It shows price estimates for ride-sharing services like Uber, Ola, and Rapido, including ratings for users to choose the best option.
- **Walking and multi-modal routes**: It combines different transport modes like bus, walking, and cabs in an intelligent manner, providing users with more flexibility.

## Future Improvements
- **AI-based learning**: The assistant could learn user preferences over time, further personalizing recommendations.
- **Integration with Google Maps**: Integrating the assistant with Google Maps as a plug-in to provide real-time suggestions on the map.
- **Real-time traffic integration**: Incorporate live traffic data to calculate the most efficient routes based on current conditions.
- **User interface**: Add an interface where users can interact with the assistant visually on a map.

