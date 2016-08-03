
<!--add mathjax server -->
<script type="text/javascript" src="http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=default"></script>

# Game Theory Papers(IJCAI2015)

## Title: Tradeoffs between Incentive Mechanisms in Boolean Games
*authors:Vadim Levit, Zohar Komarovsky, Tal Grinshpoun,  Amnon Meisels*

Two incentive mechanisms for Boolean games: taxation schemes and side payments. both be able to ensure a pure Nash equilibriun for Boolean games.

- ***taxation mechanism:*** assumes the existence of an external agent - the principal. The principal has the ability to impose tax on agent's actions,  agent must consider both marginal costs and the tax payments when choose its actions.
- ***side payments mechanism:*** enable agents in Boolean games to sacrifice part of their payoff for some given outcome in order to convince other players to play a certain strategy.

<font color="red">**Main contributions:**</font>

1. a theoretical characterization of Boolean games for which a pure Nash equilibrium can be secured using side payments
2. theoretical comparison of the two mechanisms shows that side payments are a weaker mechanism for securing a PNE in Boolean games
3. analysis the differences between the PNEs that are secured by each of these two mechanisms
4. annlysis the differences in the overall change of cost needed to secure a stable state
5. empricipal evaluation uses *social-network-based Boolean games* which initially do not have a PNE, but for which there is at least one taxation scheme that can secure its existence.
6. use an effective distributed search algorithm for Asymmetric Distributed Constraint Optimization Problems to find the appropriate side payments


## Title:Welfare maximization in fractional hedonic games
*authors:Haris Aziz, Serge Gaspers Joachim, Gudmundsson, Juli´an Mestre*

- ***hedonic game:*** comprises a set of agents who express preferences over coalitions they are they are present in and ou
tcomes are partitions of the agents into disjoint coalitions. It provides a natural framework to study coalition formation.
- ***fractional hedonic games:*** each vertex of the network can be considered as an agent. An agent \\(i's\\) valuation \\(v_i(j)\\) of an agent \\(j\\) can be represented by the weight of the directed edge \\((i,j)\\). Agent \\(i's\\) valuation of a coalition \\(S\\) of agents is the mean valuation \\(\sum_{j\in S} v_i(j)/\mid S \mid\\) of the members of \\(S\\).

<font color="red">**Main contributions:**</font>

1. simple examples that show that utilitarian, egalitarian, and Nash welfare maximizing outcomes need not coincide, even in simple symmetric FHGs.
2. a reduction that shows that maximizing utilitarian welfare, egalitarian welfare, or Nash welfare is NP-hard, even for simple symmetric FHGs.
3. a ploynomial-time 2-approximation algorithm for maximizing the utilitarian welfare of simple symmetric FHGs.
4. a polynomialtime 4-approximation algorithm for maximizing the utilitarian welfare of symmetric FHGs.
5. a polynomial-time 3-approximation algorithm for maximizing the egalitarian welfare of simple symmetric FHGs


### [Hedonic Game Theorem](http://www.sofsem.cz/sofsem13/files/presentations/Invited/Woeginger.pdf)
**Definitions:**

- finite set N of players
- coalition = non-empty subset of N
- partition \\(\Pi\\) divides N into disjoint coalitions
- \\(\Pi (i)\\) denotes coalition in \\(\Pi\\) caontaining player \\(i \in N \\)
</br>
+ every player \\(i \in N\\) ranks all the coalitions containing i via \\(\preceq_i\\) and \\(\prec_i \\) *( that means player \\(i\\) express its preferences through \\(\prec_i or \preceq_i\\))*
+ a coalition S **blocks** a partition \\(\Pi \\), if all players \\(i \in S\\) have \\(\Pi(i) \prec S \\) and hence strictly prefer being in S to being in current coalition \\(\Pi(i)\\)

**Central definition:**
A partition \\(\Pi\\) is core stable, if there is no blocking coalition S.
**Closely related:**
 weakly blocking coalition( no player worse; at least one player better off)
 strongly core stable partition( no weakly blocking cailition)

<font color="red">Problem: </font>
Given: set N with all the preferences of the players.
Decide whether there exists some core stable partition.

**Main problem:** \\(\exists\Pi\quad\forall S : \lnot (S\quad blocks \quad \Pi)\\)

**Compansion problem:** Given game andpartition, decide whether there is a blocking coalition.
\\(\Longleftrightarrow\quad \exists S:(S\quad blocks\quad \Pi)\\)
\\(\Longleftrightarrow\\) Negation of inner problem \\(\forall S:\lnot(S\quad blocks\quad \Pi))\\)

**Observation:**
If the companion problen is solveable in polynomial time, then the main paoblem is contained in NP.

**The Ballester encoding**
...

**Preferences from graphs**
*friend-oriented*
[Dimitrov, Borm, Hendrickx, Sung,2006] propose preference structures based on directed graphs G = (N,A).
An arch\\((x,y)\\) from player \\(x\\) to \\(y\\) means that \\(x\\) considers \\(y\\) as a friend. Set \\(F_x\\) contains the friends of player \\(x\\), and set \\(E_x\\) contains his enemies.
**Definitions**
Player \\(x\\) prefers S to T ("\\(S \prec_x T\\)"), if and only if

1. \\(|S \cap F_x|>|T \cap F_x|\\) or
2. \\(|S \cap F_x|=|T \cap F_x|\\) and \\(|S \cap E_x|\le|T \cap E_x|\\)

**Theorem:** under friend-oriented preferences, there always is a core stable partition.([Proof:11/30](http://www.sofsem.cz/sofsem13/files/presentations/Invited/Woeginger.pdf))

*enemy-oriented*
[Dimitrov & al 2006] also discuss graph-based enemy-oriented preferences.
**Definitions**
Player \\(x\\) prefers S to T ("\\(S \prec_x T\\)"), if and only if

1. \\(|S \cap E_x|<|T \cap E_x|\\) or
2. \\(|S \cap E_x|=|T \cap E_x|\\) and \\(|S \cap F_x|\ge|T \cap F_x|\\)

If player \\(x\\) does not like player \\(y\\), then in any core partition \\(x\\) and \\(y\\) must not in the same coalition.
Assume that friendship is mutual/symmetric, and use undirected graphs. In core stable partition, every coalition is a clique.

**Theorem**

- Under enemy-oriented preferences, there always is a core stable partition.
- Under enemy-oriented preferences, compansion-probelm is NP-complete.([Proof:13/30](http://www.sofsem.cz/sofsem13/files/presentations/Invited/Woeginger.pdf))






## Title:Simultaneous abstraction and equilibrium finding in games
*authors:Noam Brown, Tuomas Sandholm*

***Leading approach to find Nash equilibrium:*** find a Nash equilibrium in a smaller abstract version of the game that includes only a few actions at each decision point, and then map the solution back to the original game. Because a central challenge in solving imperfect-information games is that the game be far too large to solve with an equilibrium-finding algorithm.
***challenge:***

- it is difficult to determine which states should be abstracted without knowing the equilibrium of the game.
- a given abstraction is good in the equilibrium-finding process only for some time.
 ***related work:***
- most of the work focused on information abstraction.
- less of the work focused on action abstraction, and none of those approaches change the number of actions in the abstraction and thus cannot be used for growing (refining) an abstraction.

<font color="red">**Main contributions:**</font>

this paper presents an algorithm that intertwines action abstraction and equilibrium finding, which does not require knowledge of how long the equilibrium-finding algorithm will be allowed to run. It can quickly—in constant time—add actions to the abstraction while provably not having to restart the equilibrium finding.


## Title:Security Games with Information Leakage: Modeling and Computation
*authors:Haifeng Xu, Albert X. Jiang, Arunesh Sinha, Zinovi Rabinovich, Shaddin Dughmi, Milind Tambe*

***Stackelberg Game:*** leader/follower game, is a strategic game in economics in which the leader moves first and the follower move sequentially based on leaders stategies.
Most models assume that the attacker is not able to observe (even partially) the defender’s instantiated pure strategy. This falis to capture the attacker's real-time surveillance.
This paper consider information leakage in standard security game models, where the attack is instantaneous and cannot be interrupted by the defender’s resource re-allocation. To be more realistic , information is leaked from a limited of targets.
***Model:*** the defender allocates \\(k\\) resources to protect \\(n\\) targets without any scheduling constraint.
But it is difficult to solve even for basic case, so this paper approach the problem from three directions: efficient algorithms for special cases, approximation algorithms and heuristic algorithms for sampling that improves upon the status quo.


## Title:Equilibrium Analysis of Multi-Defender Security Games
*authors:Jian Lou and Yevgeniy Vorobeychik*

***A number of approaches:*** computing Stackelberg equilibria in games with a single defender protecting a collection of targets, each defender protects more than a single target.
This paper fill this gap by considering a multi-defender securrity game, focus on theretical characterizations of equilibria and the price of anarchy. This apply in non-cooperative security scenarios. Analysis focused on three models of such multi-defender games, with defenders acting non-cooperatively in all of these.
***conclusions:***

- a Nash equilibrium among defenders in this two-stage game model need not always exist, even when the defenders utilize randomized strategies.
- when an equilibrium does exist, we show that the defenders protect all of their targets with probability 1 in all three models, whereas the socially optimal protection levels are generally significantly lower.
- when no equilibrium exists, we characterize the best approximate Nash equilibrium.
- price of anarchy (PoA) analysis, which relies on the unique equilibrium when
it exists, and the approximate equilibrium otherwise,demonstrates that whereas PoA is unbounded in the simpler models, increasing linearly with the number of
defenders.
- more general model shows PoA tends to a constant as the number of defenders increases.


## Title:Computing Optimal Mixed Strategies for Security Games with Dynamic Payoffs
*authors:Jian Lou and Yevgeniy Vorobeychik*

Most work assumes that the payoffs of targets are static over time. Some work considers changing target values, but they only allow the security agency or the adversary to move at predefined, discretized time points. Proposed algorithm can only compute optimal pure defender strategies, which is unsuitable in domains where the attacker can easily conduct surveillance and recognize patterns of the defender’s pure strategy.

<font color="red">**Main contributions:**</font>

1. propose a new Stackelberg game model, in which the targets’ weights are time-dependent and both the defender and the attacker can act at any time.
2. propose COCO (Computing Optimal Coverage withOut transfer time) to compute the optimal mixed strategy for the defender when she can move security resources among targets without time delay.
3. an approach to sample pure strategies based on the compact mixed strategy computed by Proceedings of the COCO.
4. an efficient approximation scheme to compute the near optimal defender mixed strategy.
5. detailed experimental analysis on the solution quality and computational efficiency of the proposed algorithms. 



